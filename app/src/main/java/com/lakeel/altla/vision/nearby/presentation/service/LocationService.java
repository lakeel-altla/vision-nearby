package com.lakeel.altla.vision.nearby.presentation.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveDeviceLocationUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveLocationDataUseCase;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ServiceModule;
import com.lakeel.altla.vision.nearby.presentation.view.intent.IntentKey;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

public final class LocationService extends IntentService {

    private class ConnectionCallback implements GoogleApiClient.ConnectionCallbacks {

        private final Context context;

        private final String userId;

        private final String beaconId;

        ConnectionCallback(Context context, String userId, String beaconId) {
            this.context = context;
            this.userId = userId;
            this.beaconId = beaconId;
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            getUserCurrentLocation(context)
                    .toObservable()
                    .filter(location -> location != null)
                    .flatMap(LocationService.this::saveDeviceLocation)
                    .subscribe(uniqueId -> saveLocationMetaData(uniqueId, userId, beaconId), new ErrorAction<>());
        }

        @Override
        public void onConnectionSuspended(int i) {
        }
    }

    @Inject
    SaveDeviceLocationUseCase saveDeviceLocationUseCase;

    @Inject
    SaveLocationDataUseCase saveLocationDataUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

    private GoogleApiClient googleApiClient;

    // This constructor is need.
    public LocationService() {
        this(LocationService.class.getSimpleName());
    }

    public LocationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ServiceComponent component = DaggerServiceComponent.builder()
                .serviceModule(new ServiceModule(getApplicationContext()))
                .build();
        component.inject(this);

        String beaconId = intent.getStringExtra(IntentKey.BEACON_ID.name());
        String userId = intent.getStringExtra(IntentKey.USER_ID.name());
        Context context = getApplicationContext();

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .addConnectionCallbacks(new ConnectionCallback(context, userId, beaconId))
                .build();

        googleApiClient.connect();
    }

    private Single<Location> getUserCurrentLocation(Context context) {
        return Single.create(subscriber -> {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Awareness.SnapshotApi.getLocation(googleApiClient)
                        .setResultCallback(result -> {
                            if (!result.getStatus().isSuccess()) {
                                LOGGER.error("Could not get user location. User may be turning off the gps.");
                                subscriber.onSuccess(null);
                                return;
                            }

                            Location location = result.getLocation();
                            subscriber.onSuccess(location);
                        });
            } else {
                LOGGER.warn("Location permission is not granted.");
                subscriber.onSuccess(null);
            }
        });
    }

    private Observable<String> saveDeviceLocation(Location location) {
        return saveDeviceLocationUseCase.execute(location).toObservable();
    }

    private void saveLocationMetaData(String uniqueId, String userId, String beaconId) {
        saveLocationDataUseCase.execute(uniqueId, userId, beaconId).subscribe();
    }
}

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
import com.lakeel.altla.vision.nearby.domain.usecase.SaveLocationDataUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveLocationUseCase;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.ServiceComponent;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class LocationService extends IntentService {

    @Inject
    SaveLocationUseCase saveLocationUseCase;

    @Inject
    SaveLocationDataUseCase saveLocationDataUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

    private GoogleApiClient googleApiClient;

    public LocationService() {
        super(LocationService.class.getSimpleName());
    }

    public LocationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ServiceComponent component = DaggerServiceComponent.create();
        component.inject(this);

        String beaconId = intent.getStringExtra(IntentKey.BEACON_ID.name());
        Context context = getApplicationContext();

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        getUserCurrentLocation(context)
                                .flatMap(location -> saveLocationUseCase.execute(location).subscribeOn(Schedulers.io()))
                                .flatMap(uniqueId -> saveLocationDataUseCase.execute(uniqueId, beaconId).subscribeOn(Schedulers.io()))
                                .subscribeOn(Schedulers.io())
                                .subscribe(aVoid -> LOGGER.debug("Succeeded to save location data."),
                                        e -> LOGGER.error("Failed to save location data.", e));
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                })
                .build();

        googleApiClient.connect();
    }

    Single<Location> getUserCurrentLocation(Context context) {
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
}

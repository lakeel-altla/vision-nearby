package com.lakeel.profile.notification.presentation.service;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.lakeel.profile.notification.data.entity.LocationsDataEntity;
import com.lakeel.profile.notification.data.execption.DataStoreException;
import com.lakeel.profile.notification.data.mapper.LocationsDataEntityMapper;
import com.lakeel.profile.notification.presentation.intent.IntentKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import rx.Single;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class LocationService extends IntentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

    private static final String REFERENCE_GEOFENCES = "locations";

    private static final String REFERENCE_LOCATIONS = "locations-data";

    private final DatabaseReference mGeoReference = FirebaseDatabase.getInstance().getReference(REFERENCE_GEOFENCES);

    private final DatabaseReference mLocationReference = FirebaseDatabase.getInstance().getReference(REFERENCE_LOCATIONS);

    private final GeoFire geoFire = new GeoFire(mGeoReference);

    private final LocationsDataEntityMapper mLocationsDataEntityMapper = new LocationsDataEntityMapper();

    private GoogleApiClient mGoogleApiClient;

    public LocationService() {
        super(LocationService.class.getSimpleName());
    }

    public LocationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LOGGER.debug("Location Service");

        final String id = intent.getStringExtra(IntentKey.USER_ID.name());

        final Context context = getApplicationContext();
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Awareness.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        final String uniqueId = mLocationReference.push().getKey();

                        getUserCurrentLocation(context)
                                .flatMap(location -> saveGeoFence(uniqueId, location))
                                .flatMap(new Func1<Location, Single<Void>>() {
                                    @Override
                                    public Single<Void> call(Location location) {
                                        return saveLocation(uniqueId, id);
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .subscribe(aVoid -> LOGGER.debug("Succeeded to save to locations."),
                                        e -> LOGGER.error("Failed to save to locations.", e));
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                })
                .build();
        mGoogleApiClient.connect();
    }

    Single<Location> getUserCurrentLocation(Context context) {
        return Single.create(subscriber -> {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Awareness.SnapshotApi.getLocation(mGoogleApiClient)
                        .setResultCallback(locationResult -> {
                            if (!locationResult.getStatus().isSuccess()) {
                                LOGGER.error("Could not get user location. User may be turning off the location.");
                                subscriber.onSuccess(null);
                                return;
                            }
                            Location location = locationResult.getLocation();
                            subscriber.onSuccess(location);
                        });
            } else {
                LOGGER.warn("Location permission is not granted.");
                subscriber.onSuccess(null);
            }
        });
    }

    Single<Void> saveLocation(String uniqueId, String id) {
        return Single.create(subscriber -> {
            LocationsDataEntity entity = mLocationsDataEntityMapper.map(id);
            Task task = mLocationReference
                    .child(uniqueId)
                    .setValue(entity.toMap())
                    .addOnSuccessListener(subscriber::onSuccess)
                    .addOnFailureListener(e -> LOGGER.error("Failed to save user current location.", e));

            Exception exception = task.getException();
            if (exception != null) {
                subscriber.onError(new DataStoreException(exception));
            }
        });
    }

    Single<Location> saveGeoFence(String uniqueId, Location location) {
        return Single.create(subscriber ->
                geoFire.setLocation(uniqueId, new GeoLocation(location.getLatitude(), location.getLongitude()), (key, error) -> {
                    if (error == null) {
                        subscriber.onSuccess(location);
                    } else {
                        subscriber.onError(error.toException());
                    }
                }));
    }
}

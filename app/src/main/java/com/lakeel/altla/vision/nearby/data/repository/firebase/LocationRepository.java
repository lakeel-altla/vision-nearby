package com.lakeel.altla.vision.nearby.data.repository.firebase;


import android.location.Location;
import android.support.annotation.NonNull;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import rx.Single;

public class LocationRepository {

    private final GeoFire geoFire;

    @Inject
    public LocationRepository(String url) {
        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReferenceFromUrl(url));
    }

    public Single<GeoLocation> find(@NonNull String locationMetaDataId) {
        return Single.create(subscriber ->
                geoFire.getLocation(locationMetaDataId, new LocationCallback() {

                    @Override
                    public void onLocationResult(String key, GeoLocation location) {
                        if (location == null) {
                            subscriber.onSuccess(null);
                        } else {
                            subscriber.onSuccess(location);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(databaseError.toException());
                    }
                }));
    }

    public Single<String> save(@NonNull Location location) {
        String pushedKey = geoFire.getDatabaseReference().push().getKey();

        return Single.create(subscriber ->
                geoFire.setLocation(pushedKey, new GeoLocation(location.getLatitude(), location.getLongitude()), (key, error) -> {
                    if (error == null) {
                        subscriber.onSuccess(pushedKey);
                    } else {
                        subscriber.onError(error.toException());
                    }
                }));
    }
}
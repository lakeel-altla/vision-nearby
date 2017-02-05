package com.lakeel.altla.vision.nearby.data.repository;


import android.location.Location;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import rx.Single;

public class FirebaseLocationRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/locations";

    private final GeoFire geoFire;

    @Inject
    FirebaseLocationRepository() {
        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference(DATABASE_URI));
    }

    public Single<GeoLocation> findLocation(String key) {
        return Single.create(subscriber ->
                geoFire.getLocation(key, new LocationCallback() {
                    @Override
                    public void onLocationResult(String key1, GeoLocation location) {
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

    public Single<String> saveLocation(Location location) {
        String uniqueId = geoFire.getDatabaseReference().push().getKey();
        return Single.create(subscriber ->
                geoFire.setLocation(uniqueId, new GeoLocation(location.getLatitude(), location.getLongitude()), (key, error) -> {
                    if (error == null) {
                        subscriber.onSuccess(uniqueId);
                    } else {
                        subscriber.onError(error.toException());
                    }
                }));
    }
}

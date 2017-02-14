package com.lakeel.altla.vision.nearby.data.repository.firebase;


import android.location.Location;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import rx.Single;

public class LocationRepository {

    private static final String DATABASE_URI = "https://profile-notification-95441.firebaseio.com/locations";

    private final GeoFire geoFire;

    @Inject
    LocationRepository() {
        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReferenceFromUrl(DATABASE_URI));
    }

    public Single<GeoLocation> find(String locationKey) {
        return Single.create(subscriber ->
                geoFire.getLocation(locationKey, new LocationCallback() {
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

    public Single<String> save(Location location) {
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
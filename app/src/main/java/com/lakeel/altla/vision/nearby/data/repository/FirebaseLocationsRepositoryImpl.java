package com.lakeel.altla.vision.nearby.data.repository;


import android.location.Location;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLocationsRepository;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;

public class FirebaseLocationsRepositoryImpl implements FirebaseLocationsRepository {

    private GeoFire geoFire;

    @Inject
    public FirebaseLocationsRepositoryImpl(String url) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
        geoFire = new GeoFire(reference);
    }

    @Override
    public Single<GeoLocation> findLocationByKey(String key) {
        return Single.create(new Single.OnSubscribe<GeoLocation>() {
            @Override
            public void call(SingleSubscriber<? super GeoLocation> subscriber) {
                geoFire.getLocation(key, new LocationCallback() {
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
                });
            }
        });
    }

    @Override
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

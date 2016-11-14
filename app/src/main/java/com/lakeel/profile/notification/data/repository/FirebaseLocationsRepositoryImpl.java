package com.lakeel.profile.notification.data.repository;


import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.lakeel.profile.notification.domain.repository.FirebaseLocationsRepository;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;

public class FirebaseLocationsRepositoryImpl implements FirebaseLocationsRepository {

    private GeoFire mGeoFire;

    @Inject
    public FirebaseLocationsRepositoryImpl(String url) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl(url);
        mGeoFire = new GeoFire(reference);
    }

    @Override
    public Single<GeoLocation> findLocationByKey(String key) {
        return Single.create(new Single.OnSubscribe<GeoLocation>() {
            @Override
            public void call(SingleSubscriber<? super GeoLocation> subscriber) {
                mGeoFire.getLocation(key, new LocationCallback() {
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
}

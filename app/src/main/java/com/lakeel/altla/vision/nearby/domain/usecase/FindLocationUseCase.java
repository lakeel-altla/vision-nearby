package com.lakeel.altla.vision.nearby.domain.usecase;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLocationsRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindLocationUseCase {

    @Inject
    FirebaseLocationsRepository mFirebaseLocationsRepository;

    @Inject
    FindLocationUseCase() {
    }

    public Single<GeoLocation> execute(String key) {
        return mFirebaseLocationsRepository.findLocationByKey(key);
    }
}
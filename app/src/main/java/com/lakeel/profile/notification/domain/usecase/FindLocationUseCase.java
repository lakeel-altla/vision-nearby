package com.lakeel.profile.notification.domain.usecase;

import com.firebase.geofire.GeoLocation;
import com.lakeel.profile.notification.domain.repository.FirebaseLocationsRepository;

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

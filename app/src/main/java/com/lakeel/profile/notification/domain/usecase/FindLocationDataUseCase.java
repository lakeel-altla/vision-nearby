package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.data.entity.LocationsDataEntity;
import com.lakeel.profile.notification.domain.repository.FirebaseLocationsDataRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindLocationDataUseCase {

    @Inject
    FirebaseLocationsDataRepository mFirebaseLocationsDataRepository;

    @Inject
    FindLocationDataUseCase() {
    }

    public Single<LocationsDataEntity> execute(String id) {
        return mFirebaseLocationsDataRepository.findLocationsDataById(id);
    }
}

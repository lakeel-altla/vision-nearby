package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.LocationsDataEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLocationsDataRepository;

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

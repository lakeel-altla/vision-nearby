package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.LocationDataEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLocationsDataRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindLocationDataUseCase {

    @Inject
    FirebaseLocationsDataRepository mFirebaseLocationsDataRepository;

    @Inject
    FindLocationDataUseCase() {
    }

    public Single<LocationDataEntity> execute(String beaconId) {
        return mFirebaseLocationsDataRepository.findLocationsDataByBeaconId(beaconId);
    }
}

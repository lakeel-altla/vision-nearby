package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.LocationsDataEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLocationsDataRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveLocationDataUseCase {

    @Inject
    FirebaseLocationsDataRepository repository;

    @Inject
    SaveLocationDataUseCase() {
    }

    public Single<LocationsDataEntity> execute(String uniqueId, String beaconId) {
        return repository.saveLocationData(uniqueId, beaconId);
    }
}

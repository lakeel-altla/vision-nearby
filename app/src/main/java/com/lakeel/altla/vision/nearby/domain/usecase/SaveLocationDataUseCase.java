package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.LocationDataEntity;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseLocationsDataRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveLocationDataUseCase {

    @Inject
    FirebaseLocationsDataRepository repository;

    @Inject
    SaveLocationDataUseCase() {
    }

    public Single<LocationDataEntity> execute(String uniqueId, String beaconId) {
        return repository.saveLocationData(uniqueId, beaconId).subscribeOn(Schedulers.io());
    }
}

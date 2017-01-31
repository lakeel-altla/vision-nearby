package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserLocationMetaDataRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveLocationDataUseCase {

    @Inject
    FirebaseUserLocationMetaDataRepository repository;

    @Inject
    SaveLocationDataUseCase() {
    }

    public Completable execute(String uniqueId, String userId, String beaconId) {
        return repository.saveLocationMetaData(uniqueId, userId, beaconId).subscribeOn(Schedulers.io());
    }
}

package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserLocationMetaDataRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveLocationMetaDataUseCase {

    @Inject
    UserLocationMetaDataRepository repository;

    @Inject
    SaveLocationMetaDataUseCase() {
    }

    public Completable execute(String uniqueId, String userId, String beaconId) {
        return repository.save(uniqueId, userId, beaconId).subscribeOn(Schedulers.io());
    }
}

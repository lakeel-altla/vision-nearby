package com.lakeel.altla.vision.nearby.domain.usecase;

import com.google.firebase.database.ServerValue;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserLocationMetaDataRepository;
import com.lakeel.altla.vision.nearby.domain.model.LocationMeta;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveLocationMetaDataUseCase {

    @Inject
    UserLocationMetaDataRepository repository;

    @Inject
    SaveLocationMetaDataUseCase() {
    }

    public Completable execute(String userId, String locationMetaDataId, String beaconId) {
        LocationMeta locationMeta = new LocationMeta();
        locationMeta.userId = userId;
        locationMeta.locationMetaDataId = locationMetaDataId;
        locationMeta.beaconId = beaconId;
        locationMeta.passingTime = ServerValue.TIMESTAMP;
        return repository.save(locationMeta).subscribeOn(Schedulers.io());
    }
}
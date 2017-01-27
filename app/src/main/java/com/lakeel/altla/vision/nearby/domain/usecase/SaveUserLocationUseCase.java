package com.lakeel.altla.vision.nearby.domain.usecase;

import android.location.Location;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseHistoryRepository;
import com.lakeel.altla.vision.nearby.domain.entity.HistoryEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveUserLocationUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    SaveUserLocationUseCase() {
    }

    public Single<HistoryEntity> execute(String uniqueId, String userId, Location location) {
        return repository.saveCurrentLocation(uniqueId, userId, location).subscribeOn(Schedulers.io());
    }
}

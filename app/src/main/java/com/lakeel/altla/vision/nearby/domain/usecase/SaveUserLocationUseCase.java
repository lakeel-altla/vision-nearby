package com.lakeel.altla.vision.nearby.domain.usecase;

import android.location.Location;

import com.lakeel.altla.vision.nearby.domain.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseHistoryRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveUserLocationUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    SaveUserLocationUseCase() {
    }

    public Single<HistoryEntity> execute(String uniqueId, String userId, Location location) {
        return repository.saveCurrentLocation(uniqueId, userId, location);
    }
}

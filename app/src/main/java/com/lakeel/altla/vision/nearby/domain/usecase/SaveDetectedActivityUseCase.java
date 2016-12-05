package com.lakeel.altla.vision.nearby.domain.usecase;

import com.google.android.gms.location.DetectedActivity;
import com.lakeel.altla.vision.nearby.data.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseHistoryRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveDetectedActivityUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    SaveDetectedActivityUseCase() {
    }

    public Single<HistoryEntity> execute(String uniqueKey, String userId, DetectedActivity detectedActivity) {
        return repository.saveDetectedActivity(uniqueKey, userId, detectedActivity);
    }
}

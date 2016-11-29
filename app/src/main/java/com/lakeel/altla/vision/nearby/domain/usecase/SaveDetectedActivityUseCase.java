package com.lakeel.altla.vision.nearby.domain.usecase;

import com.google.android.gms.location.DetectedActivity;
import com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseRecentlyRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveDetectedActivityUseCase {

    @Inject
    FirebaseRecentlyRepository repository;

    @Inject
    SaveDetectedActivityUseCase() {
    }

    public Single<RecentlyEntity> execute(String uniqueKey, DetectedActivity detectedActivity) {
        return repository.saveDetectedActivity(uniqueKey, detectedActivity);
    }
}

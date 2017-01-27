package com.lakeel.altla.vision.nearby.domain.usecase;

import com.google.android.gms.location.DetectedActivity;
import com.lakeel.altla.vision.nearby.domain.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseHistoryRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveUserActivityUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    SaveUserActivityUseCase() {
    }

    public Single<HistoryEntity> execute(String uniqueId, String userId, DetectedActivity userActivity) {
        return repository.saveUserActivity(uniqueId, userId, userActivity).subscribeOn(Schedulers.io());
    }
}

package com.lakeel.altla.vision.nearby.domain.usecase;

import com.google.android.gms.location.DetectedActivity;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.CurrentUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveUserActivityUseCase {

    @Inject
    UserNearbyHistoryRepository repository;

    @Inject
    SaveUserActivityUseCase() {
    }

    public Completable execute(String uniqueId, DetectedActivity userActivity) {
        String userId = CurrentUser.getUid();
        return repository.saveUserActivity(uniqueId, userId, userActivity).subscribeOn(Schedulers.io());
    }
}

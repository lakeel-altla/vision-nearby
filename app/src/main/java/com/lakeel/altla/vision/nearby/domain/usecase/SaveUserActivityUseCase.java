package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.google.android.gms.location.DetectedActivity;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveUserActivityUseCase {

    @Inject
    UserNearbyHistoryRepository repository;

    @Inject
    SaveUserActivityUseCase() {
    }

    public Completable execute(@NonNull String historyId, @NonNull DetectedActivity userActivity) {
        String userId = CurrentUser.getUid();
        return repository.saveUserActivity(userId, historyId, userActivity).subscribeOn(Schedulers.io());
    }
}

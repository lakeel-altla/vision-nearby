package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class RemoveNearbyHistoryUseCase {

    @Inject
    UserNearbyHistoryRepository repository;

    @Inject
    RemoveNearbyHistoryUseCase() {
    }

    public Completable execute(@NonNull String historyId) {
        String userId = CurrentUser.getUid();
        return repository.remove(userId, historyId).subscribeOn(Schedulers.io());
    }
}

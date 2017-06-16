package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindLatestNearbyHistoryUseCase {

    @Inject
    UserNearbyHistoryRepository repository;

    @Inject
    FindLatestNearbyHistoryUseCase() {
    }

    public Single<NearbyHistory> execute(@NonNull String favoriteUserId) {
        String userId = CurrentUser.getUid();
        return repository.findLatest(userId, favoriteUserId).subscribeOn(Schedulers.io());
    }
}
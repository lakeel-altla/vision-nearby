package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindNearbyHistoryUseCase {

    @Inject
    UserNearbyHistoryRepository historyRepository;

    @Inject
    FindNearbyHistoryUseCase() {
    }

    public Single<NearbyHistory> execute(@NonNull String historyId) {
        String userId = CurrentUser.getUid();
        return historyRepository.find(userId, historyId).subscribeOn(Schedulers.io());
    }
}

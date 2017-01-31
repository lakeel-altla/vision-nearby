package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindNearbyHistoryUseCase {

    @Inject
    FirebaseUserNearbyHistoryRepository historyRepository;

    @Inject
    FindNearbyHistoryUseCase() {
    }

    public Single<NearbyHistory> execute(String historyId) {
        String userId = MyUser.getUserId();
        return historyRepository.findNearbyHistory(userId, historyId).subscribeOn(Schedulers.io());
    }
}

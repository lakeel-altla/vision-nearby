package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindRecentNearbyHistoryUseCase {

    @Inject
    FirebaseUserNearbyHistoryRepository repository;

    @Inject
    FindRecentNearbyHistoryUseCase() {
    }

    public Single<NearbyHistory> execute(String favoriteUserId) {
        String userId = MyUser.getUserId();
        return repository.findRecently(userId, favoriteUserId).subscribeOn(Schedulers.io());
    }
}
package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.domain.model.NearbyHistory;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindLatestNearbyHistoryUseCase {

    @Inject
    UserNearbyHistoryRepository repository;

    @Inject
    FindLatestNearbyHistoryUseCase() {
    }

    public Single<NearbyHistory> execute(String favoriteUserId) {
        String userId = MyUser.getUserId();
        return repository.findLatest(userId, favoriteUserId).subscribeOn(Schedulers.io());
    }
}
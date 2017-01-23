package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseHistoryRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindHistoryUseCase {

    @Inject
    FirebaseHistoryRepository historyRepository;

    @Inject
    FindHistoryUseCase() {
    }

    public Single<HistoryEntity> execute(String historyId) {
        String userId = MyUser.getUserId();
        return historyRepository.findHistory(userId, historyId).subscribeOn(Schedulers.io());
    }
}

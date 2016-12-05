package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseHistoryRepository;

import javax.inject.Inject;

import rx.Observable;

public final class FindHistoryUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    FindHistoryUseCase() {
    }

    public Observable<HistoryEntity> execute(String userId) {
        return repository.findHistoryByUserId(userId);
    }
}

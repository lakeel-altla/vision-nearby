package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseRecentlyRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindHistoryUseCase {

    @Inject
    FirebaseRecentlyRepository repository;

    @Inject
    FindHistoryUseCase() {
    }

    public Single<RecentlyEntity> execute(String userId, String uniqueKey) {
        return repository.findHistoryByUserIdAndUniqueKey(userId, uniqueKey);
    }
}

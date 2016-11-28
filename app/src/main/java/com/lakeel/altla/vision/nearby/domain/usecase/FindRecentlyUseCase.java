package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseRecentlyRepository;

import javax.inject.Inject;

import rx.Observable;

public final class FindRecentlyUseCase {

    @Inject
    FirebaseRecentlyRepository mFirebaseRecentlyRepository;

    @Inject
    FindRecentlyUseCase() {
    }

    public Observable<RecentlyEntity> execute() {
        return mFirebaseRecentlyRepository.findRecently();
    }
}

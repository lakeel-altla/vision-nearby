package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseItemsRepository;

import javax.inject.Inject;

import rx.Observable;

public final class FindUserBeaconsUseCase {

    @Inject
    FirebaseItemsRepository repository;

    @Inject
    FindUserBeaconsUseCase() {
    }

    public Observable<String> execute(String userId) {
        return repository.findBeaconsByUserId(userId);
    }
}

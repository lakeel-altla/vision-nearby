package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindUserBeaconsUseCase {

    @Inject
    FirebaseUsersRepository repository;

    @Inject
    FindUserBeaconsUseCase() {
    }

    public Observable<String> execute(String userId) {
        return repository.findBeaconsByUserId(userId).subscribeOn(Schedulers.io());
    }
}

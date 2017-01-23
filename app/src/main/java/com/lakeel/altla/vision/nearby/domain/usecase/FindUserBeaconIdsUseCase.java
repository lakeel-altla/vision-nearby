package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUsersRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindUserBeaconIdsUseCase {

    @Inject
    FirebaseUsersRepository repository;

    @Inject
    FindUserBeaconIdsUseCase() {
    }

    public Observable<String> execute(String userId) {
        return repository.findBeaconsByUserId(userId).subscribeOn(Schedulers.io());
    }
}

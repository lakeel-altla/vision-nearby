package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserProfileRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindUserBeaconsUseCase {

    @Inject
    FirebaseUserProfileRepository repository;

    @Inject
    FindUserBeaconsUseCase() {
    }

    public Observable<String> execute(String userId) {
        return repository.findUserBeacons(userId).subscribeOn(Schedulers.io());
    }
}

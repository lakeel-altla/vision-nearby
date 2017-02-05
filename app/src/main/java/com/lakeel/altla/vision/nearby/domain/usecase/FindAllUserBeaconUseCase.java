package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindAllUserBeaconUseCase {

    @Inject
    UserProfileRepository repository;

    @Inject
    FindAllUserBeaconUseCase() {
    }

    public Observable<String> execute(String userId) {
        return repository.findUserBeacons(userId).subscribeOn(Schedulers.io());
    }
}

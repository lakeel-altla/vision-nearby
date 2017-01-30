package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.User;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindUserUseCase {

    @Inject
    FirebaseUserProfileRepository repository;

    @Inject
    FindUserUseCase() {
    }

    public Single<User> execute(String userId) {
        return repository.findUser(userId).subscribeOn(Schedulers.io());
    }
}

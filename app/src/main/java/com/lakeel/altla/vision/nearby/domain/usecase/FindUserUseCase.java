package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindUserUseCase {

    @Inject
    FirebaseUsersRepository repository;

    @Inject
    FindUserUseCase() {
    }

    public Single<UserEntity> execute(String userId) {
        return repository.findUserByUserId(userId).subscribeOn(Schedulers.io());
    }
}

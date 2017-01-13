package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindUserUseCase {

    @Inject
    FirebaseUsersRepository repository;

    @Inject
    FindUserUseCase() {
    }

    public Single<UserEntity> execute(String userId) {
        return repository.findUserByUserId(userId);
    }
}

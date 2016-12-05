package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindItemNameUseCase {

    @Inject
    FirebaseUsersRepository repository;

    @Inject
    FindItemNameUseCase() {
    }

    public Single<UserEntity> execute(String name) {
        return repository.findUserByName(name);
    }
}

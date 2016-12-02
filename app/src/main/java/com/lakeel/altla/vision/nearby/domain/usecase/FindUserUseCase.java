package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;

import javax.inject.Inject;

import rx.Single;

public final class FindUserUseCase {

    @Inject
    FirebaseUsersRepository mFirebaseUsersRepository;

    @Inject
    public FindUserUseCase() {
    }

    public Single<UserEntity> execute(String userId) {
        return mFirebaseUsersRepository.findUserById(userId);
    }
}

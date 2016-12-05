package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;

import javax.inject.Inject;

import rx.Completable;

public final class SaveUserUseCase {

    @Inject
    FirebaseUsersRepository repository;

    @Inject
    public SaveUserUseCase() {
    }

    public Completable execute(String userId) {
        return repository.saveUser(userId);
    }
}

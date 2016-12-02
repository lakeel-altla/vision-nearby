package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;

import javax.inject.Inject;

import rx.Completable;

public final class SaveItemUseCase {

    @Inject
    FirebaseUsersRepository mFirebaseUsersRepository;


    @Inject
    public SaveItemUseCase() {
    }

    public Completable execute() {
        return mFirebaseUsersRepository.saveUser();
    }
}

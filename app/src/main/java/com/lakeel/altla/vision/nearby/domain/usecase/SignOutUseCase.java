package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseAuthRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SignOutUseCase {

    @Inject
    FirebaseAuthRepository mFirebaseAuthRepository;

    @Inject
    SignOutUseCase() {
    }

    public Completable execute() {
        return mFirebaseAuthRepository.signOut().subscribeOn(Schedulers.io());
    }
}

package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserProfileRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveUserUseCase {

    @Inject
    FirebaseUserProfileRepository repository;

    @Inject
    SaveUserUseCase() {
    }

    public Completable execute() {
        String userId = MyUser.getUserId();
        return repository.saveUser(userId).subscribeOn(Schedulers.io());
    }
}

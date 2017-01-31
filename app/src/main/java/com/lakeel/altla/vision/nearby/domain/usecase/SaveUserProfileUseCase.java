package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserProfileRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveUserProfileUseCase {

    @Inject
    FirebaseUserProfileRepository repository;

    @Inject
    SaveUserProfileUseCase() {
    }

    public Completable execute() {
        String userId = MyUser.getUserId();
        return repository.saveUserProfile(userId).subscribeOn(Schedulers.io());
    }
}

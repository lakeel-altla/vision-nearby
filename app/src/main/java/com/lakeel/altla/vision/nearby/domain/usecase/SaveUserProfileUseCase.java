package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveUserProfileUseCase {

    @Inject
    UserProfileRepository repository;

    @Inject
    SaveUserProfileUseCase() {
    }

    public Completable execute() {
        String userId = MyUser.getUserId();
        return repository.save(userId).subscribeOn(Schedulers.io());
    }
}

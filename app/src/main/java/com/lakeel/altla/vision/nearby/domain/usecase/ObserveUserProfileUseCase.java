package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.User;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class ObserveUserProfileUseCase {

    @Inject
    FirebaseUserProfileRepository repository;

    @Inject
    ObserveUserProfileUseCase() {
    }

    public Observable<User> execute() {
        String userId = MyUser.getUserId();
        return repository.observeUser(userId).subscribeOn(Schedulers.io());
    }
}

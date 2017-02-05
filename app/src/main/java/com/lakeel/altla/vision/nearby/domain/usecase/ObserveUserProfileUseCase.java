package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class ObserveUserProfileUseCase {

    @Inject
    UserProfileRepository repository;

    @Inject
    ObserveUserProfileUseCase() {
    }

    public Observable<UserProfile> execute() {
        String userId = MyUser.getUserId();
        return repository.observe(userId).subscribeOn(Schedulers.io());
    }
}

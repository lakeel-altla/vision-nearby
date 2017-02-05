package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindUserUseCase {

    @Inject
    UserProfileRepository repository;

    @Inject
    FindUserUseCase() {
    }

    public Single<UserProfile> execute(String userId) {
        return repository.find(userId).subscribeOn(Schedulers.io());
    }
}

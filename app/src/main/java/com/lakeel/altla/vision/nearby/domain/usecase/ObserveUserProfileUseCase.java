package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

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
        String userId = CurrentUser.getUid();
        return repository.observeProfile(userId).subscribeOn(Schedulers.io());
    }
}

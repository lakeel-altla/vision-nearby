package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class ObserveUserProfileUseCase {

    @Inject
    FirebaseUsersRepository repository;

    @Inject
    ObserveUserProfileUseCase() {
    }

    public Observable<UserEntity> execute() {
        String userId = MyUser.getUid();
        return repository.observeUserProfile(userId).subscribeOn(Schedulers.io());
    }
}

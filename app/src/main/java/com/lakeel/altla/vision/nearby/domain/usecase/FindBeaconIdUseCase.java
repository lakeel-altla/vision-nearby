package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.PreferenceRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindBeaconIdUseCase {

    @Inject
    PreferenceRepository repository;

    @Inject
    FindBeaconIdUseCase() {
    }

    public Single<String> execute() {
        String userId = MyUser.getUserId();
        return repository.findBeaconId(userId).subscribeOn(Schedulers.io());
    }
}

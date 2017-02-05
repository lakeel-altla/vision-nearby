package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserInformationRepository;

import javax.inject.Inject;

import rx.Completable;
import rx.schedulers.Schedulers;

public final class SaveInformationUseCase {

    @Inject
    UserInformationRepository repository;

    @Inject
    SaveInformationUseCase() {
    }

    public Completable execute(String userId, String title, String message) {
        return repository.save(userId, title, message).subscribeOn(Schedulers.io());
    }
}

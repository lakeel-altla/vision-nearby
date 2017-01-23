package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseInformationRepository;

import javax.inject.Inject;

import rx.Completable;

public final class SaveInformationUseCase {

    @Inject
    FirebaseInformationRepository repository;

    @Inject
    SaveInformationUseCase() {
    }

    public Completable execute(String userId, String title, String message) {
        return repository.save(userId, title, message);
    }
}

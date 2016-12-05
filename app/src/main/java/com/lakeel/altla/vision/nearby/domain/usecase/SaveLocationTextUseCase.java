package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseHistoryRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveLocationTextUseCase {

    @Inject
    FirebaseHistoryRepository repository;

    @Inject
    public SaveLocationTextUseCase() {
    }

    public Single<String> execute(String key, String userId, String language, String locationText) {
        return repository.saveLocationText(key, userId, language, locationText);
    }
}

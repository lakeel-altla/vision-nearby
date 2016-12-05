package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCMApiKeyUseCase {

    @Inject
    FirebaseCMLinksRepository repository;

    @Inject
    SaveCMApiKeyUseCase() {
    }

    public Single<String> execute(String userId, String apiKey) {
        return repository.saveCMApiKey(userId, apiKey);
    }
}

package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCmLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCmApiKeyUseCase {

    @Inject
    FirebaseCmLinksRepository repository;

    @Inject
    SaveCmApiKeyUseCase() {
    }

    public Single<String> execute(String userId, String apiKey) {
        return repository.saveApiKey(userId, apiKey);
    }
}

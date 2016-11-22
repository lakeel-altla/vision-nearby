package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCMApiKeyUseCase {

    @Inject
    FirebaseCMLinksRepository mFirebaseCMLinksRepository;

    @Inject
    SaveCMApiKeyUseCase() {
    }

    public Single<String> execute(String apiKey) {
        return mFirebaseCMLinksRepository.saveCMApiKey(apiKey);
    }
}

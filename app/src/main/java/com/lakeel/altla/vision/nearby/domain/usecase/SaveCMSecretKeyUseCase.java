package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCmLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCmSecretKeyUseCase {

    @Inject
    FirebaseCmLinksRepository repository;

    @Inject
    SaveCmSecretKeyUseCase() {
    }

    public Single<String> execute(String userId, String secretKey) {
        return repository.saveSecretKey(userId, secretKey);
    }
}

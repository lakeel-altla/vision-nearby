package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCmSecretKeyUseCase {

    @Inject
    FirebaseCMLinksRepository repository;

    @Inject
    SaveCmSecretKeyUseCase() {
    }

    public Single<String> execute(String userId, String secretKey) {
        return repository.saveSecretKey(userId, secretKey);
    }
}

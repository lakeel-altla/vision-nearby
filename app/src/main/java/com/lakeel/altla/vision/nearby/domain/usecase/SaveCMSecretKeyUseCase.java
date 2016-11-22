package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCMSecretKeyUseCase {

    @Inject
    FirebaseCMLinksRepository mFirebaseCMLinksRepository;

    @Inject
    SaveCMSecretKeyUseCase() {
    }

    public Single<String> execute(String secretKey) {
        return mFirebaseCMLinksRepository.saveCMSecretKey(secretKey);
    }
}

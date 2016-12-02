package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseTokensRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindTokenUseCase {

    @Inject
    FirebaseTokensRepository repository;

    @Inject
    FindTokenUseCase() {
    }

    public Single<String> execute(String userId) {
        return repository.findTokenByUserId(userId);
    }
}

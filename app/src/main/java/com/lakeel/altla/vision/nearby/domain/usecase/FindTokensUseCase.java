package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseTokensRepository;

import javax.inject.Inject;

import rx.Observable;

public final class FindTokensUseCase {

    @Inject
    FirebaseTokensRepository repository;

    @Inject
    FindTokensUseCase() {
    }

    public Observable<String> execute(String userId) {
        return repository.findTokensByUserId(userId);
    }
}

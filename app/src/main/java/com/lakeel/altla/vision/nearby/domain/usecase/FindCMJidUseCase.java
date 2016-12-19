package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCmLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindCmJidUseCase {

    @Inject
    FirebaseCmLinksRepository repository;

    @Inject
    FindCmJidUseCase() {
    }

    public Single<String> execute(String userId) {
        return repository.findJidByUserId(userId);
    }
}

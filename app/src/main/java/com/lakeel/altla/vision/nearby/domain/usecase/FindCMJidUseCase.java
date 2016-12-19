package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindCMJidUseCase {

    @Inject
    FirebaseCMLinksRepository repository;

    @Inject
    FindCMJidUseCase() {
    }

    public Single<String> execute(String userId) {
        return repository.findJidByUserId(userId);
    }

}

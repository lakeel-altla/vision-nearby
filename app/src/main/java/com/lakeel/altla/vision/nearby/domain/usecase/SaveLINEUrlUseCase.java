package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLINELinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveLINEUrlUseCase {

    @Inject
    FirebaseLINELinksRepository repository;

    @Inject
    SaveLINEUrlUseCase() {
    }

    public Single<String> execute(String userId, String url) {
        return repository.saveUrl(userId, url);
    }
}

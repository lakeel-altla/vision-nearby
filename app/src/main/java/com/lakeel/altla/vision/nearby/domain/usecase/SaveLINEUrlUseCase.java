package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLineLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveLineUrlUseCase {

    @Inject
    FirebaseLineLinksRepository mFirebaseLineLinksRepository;

    @Inject
    SaveLineUrlUseCase() {
    }

    public Single<String> execute(String userId, String url) {
        return mFirebaseLineLinksRepository.saveUrl(userId, url);
    }
}

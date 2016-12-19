package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCmJidUseCase {

    @Inject
    FirebaseCMLinksRepository repository;

    @Inject
    SaveCmJidUseCase() {
    }

    public Single<String> execute(String userId, String jid) {
        return repository.saveJid(userId, jid);
    }
}

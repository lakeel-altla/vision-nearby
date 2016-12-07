package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCmLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCmJidUseCase {

    @Inject
    FirebaseCmLinksRepository repository;

    @Inject
    SaveCmJidUseCase() {
    }

    public Single<String> execute(String userId, String jid) {
        return repository.saveJid(userId, jid);
    }
}

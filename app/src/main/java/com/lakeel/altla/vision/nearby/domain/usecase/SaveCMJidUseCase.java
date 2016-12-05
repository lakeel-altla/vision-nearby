package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCMJidUseCase {

    @Inject
    FirebaseCMLinksRepository mFirebaseCMLinksRepository;

    @Inject
    SaveCMJidUseCase() {
    }

    public Single<String> execute(String userId, String jid) {
        return mFirebaseCMLinksRepository.saveCMJid(userId, jid);
    }
}

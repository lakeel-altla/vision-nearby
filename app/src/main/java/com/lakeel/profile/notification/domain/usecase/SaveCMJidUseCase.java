package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCMJidUseCase {

    @Inject
    FirebaseCMLinksRepository mFirebaseCMLinksRepository;

    @Inject
    SaveCMJidUseCase() {
    }

    public Single<String> execute(String jid) {
        return mFirebaseCMLinksRepository.saveCMJid(jid);
    }
}

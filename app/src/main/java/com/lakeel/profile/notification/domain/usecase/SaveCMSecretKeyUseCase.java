package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCMSecretKeyUseCase {

    @Inject
    FirebaseCMLinksRepository mFirebaseCMLinksRepository;

    @Inject
    SaveCMSecretKeyUseCase() {
    }

    public Single<String> execute(String secretKey) {
        return mFirebaseCMLinksRepository.saveCMSecretKey(secretKey);
    }
}

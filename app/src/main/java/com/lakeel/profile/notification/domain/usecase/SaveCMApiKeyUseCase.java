package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.domain.repository.FirebaseCMLinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCMApiKeyUseCase {

    @Inject
    FirebaseCMLinksRepository mFirebaseCMLinksRepository;

    @Inject
    SaveCMApiKeyUseCase() {
    }

    public Single<String> execute(String apiKey) {
        return mFirebaseCMLinksRepository.saveCMApiKey(apiKey);
    }
}

package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.domain.repository.FirebaseLINELinksRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveLINEUrlUseCase {

    @Inject
    FirebaseLINELinksRepository mFirebaseLINELinksRepository;

    @Inject
    SaveLINEUrlUseCase() {
    }

    public Single<String> execute(String url) {
        return mFirebaseLINELinksRepository.saveUrl(url);
    }
}

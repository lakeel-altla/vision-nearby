package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.domain.repository.FirebaseRecentlyRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveLocationTextUseCase {

    @Inject
    FirebaseRecentlyRepository mFirebaseRecentlyRepository;

    @Inject
    public SaveLocationTextUseCase() {
    }

    public Single<String> execute(String key, String language, String locationText) {
        return mFirebaseRecentlyRepository.saveLocationText(key, language, locationText);
    }
}

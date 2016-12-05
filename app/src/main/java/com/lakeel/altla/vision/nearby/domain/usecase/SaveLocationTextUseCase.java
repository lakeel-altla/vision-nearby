package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseRecentlyRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveLocationTextUseCase {

    @Inject
    FirebaseRecentlyRepository mFirebaseRecentlyRepository;

    @Inject
    public SaveLocationTextUseCase() {
    }

    public Single<String> execute(String key, String userId, String language, String locationText) {
        return mFirebaseRecentlyRepository.saveLocationText(key, userId, language, locationText);
    }
}

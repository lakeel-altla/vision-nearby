package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.LINELinksRepository;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveLINEUrlUseCase {

    @Inject
    LINELinksRepository repository;

    @Inject
    SaveLINEUrlUseCase() {
    }

    public Single<String> execute(String url) {
        String userId = CurrentUser.getUid();
        return repository.save(userId, url).subscribeOn(Schedulers.io());
    }
}

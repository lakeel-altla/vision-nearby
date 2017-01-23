package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLINELinksRepository;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveLINEUrlUseCase {

    @Inject
    FirebaseLINELinksRepository repository;

    @Inject
    SaveLINEUrlUseCase() {
    }

    public Single<String> execute(String url) {
        String userId = MyUser.getUserId();
        return repository.saveUrl(userId, url).subscribeOn(Schedulers.io());
    }
}

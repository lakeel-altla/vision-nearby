package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserDeviceTokenRepository;
import com.lakeel.altla.vision.nearby.domain.model.Token;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindTokensUseCase {

    @Inject
    FirebaseUserDeviceTokenRepository repository;

    @Inject
    FindTokensUseCase() {
    }

    public Observable<Token> execute(String userId) {
        return repository.findTokens(userId).subscribeOn(Schedulers.io());
    }
}


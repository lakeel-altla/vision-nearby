package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.entity.TokenEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseTokensRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class FindTokensUseCase {

    @Inject
    FirebaseTokensRepository repository;

    @Inject
    FindTokensUseCase() {
    }

    public Observable<TokenEntity> execute(String userId) {
        return repository.findTokensByUserId(userId).subscribeOn(Schedulers.io());
    }
}

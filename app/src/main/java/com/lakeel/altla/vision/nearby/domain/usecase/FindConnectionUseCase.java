package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.UserConnectionRepository;
import com.lakeel.altla.vision.nearby.domain.model.Connection;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public class FindConnectionUseCase {

    @Inject
    UserConnectionRepository repository;

    @Inject
    FindConnectionUseCase() {
    }

    public Single<Connection> execute(String userId) {
        return repository.find(userId).subscribeOn(Schedulers.io());
    }
}

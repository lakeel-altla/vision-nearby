package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

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

    public Single<Connection> execute(@NonNull String userId) {
        return repository.find(userId).subscribeOn(Schedulers.io());
    }
}

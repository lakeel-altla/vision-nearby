package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseConnectionsRepository;
import com.lakeel.altla.vision.nearby.domain.model.Presence;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public class FindConnectionUseCase {

    @Inject
    FirebaseConnectionsRepository repository;

    @Inject
    FindConnectionUseCase() {
    }

    public Single<Presence> execute(String userId) {
        return repository.findPresence(userId).subscribeOn(Schedulers.io());
    }
}

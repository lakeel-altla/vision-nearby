package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseConnectionsRepository;
import com.lakeel.altla.vision.nearby.domain.entity.PresenceEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public class FindConnectionUseCase {

    @Inject
    FirebaseConnectionsRepository repository;

    @Inject
    FindConnectionUseCase() {
    }

    public Single<PresenceEntity> execute(String userId) {
        return repository.findPresenceByUserId(userId).subscribeOn(Schedulers.io());
    }
}

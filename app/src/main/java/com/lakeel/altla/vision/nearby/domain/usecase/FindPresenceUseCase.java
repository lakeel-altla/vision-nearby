package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.entity.PresenceEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebasePresencesRepository;

import javax.inject.Inject;

import rx.Single;

public class FindPresenceUseCase {

    @Inject
    FirebasePresencesRepository repository;

    @Inject
    FindPresenceUseCase() {
    }

    public Single<PresenceEntity> execute(String id) {
        return repository.findPresenceByUserId(id);
    }
}

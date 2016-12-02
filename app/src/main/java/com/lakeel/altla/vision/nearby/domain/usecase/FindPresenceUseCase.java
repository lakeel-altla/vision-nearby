package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebasePresencesRepository;
import com.lakeel.altla.vision.nearby.data.entity.PresenceEntity;

import javax.inject.Inject;

import rx.Single;

public class FindPresenceUseCase {

    @Inject
    FirebasePresencesRepository mFirebasePresencesRepository;

    @Inject
    public FindPresenceUseCase() {
    }

    public Single<PresenceEntity> execute(String id) {
        return mFirebasePresencesRepository.findPresenceById(id);
    }
}

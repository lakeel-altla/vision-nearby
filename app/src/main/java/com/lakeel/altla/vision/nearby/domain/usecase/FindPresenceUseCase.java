package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebasePresenceRepository;
import com.lakeel.altla.vision.nearby.data.entity.PresencesEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public class FindPresenceUseCase {

    @Inject
    FirebasePresenceRepository mFirebasePresenceRepository;

    @Inject
    public FindPresenceUseCase() {
    }

    public Single<PresencesEntity> execute(String id) {
        return mFirebasePresenceRepository.findPresenceById(id);
    }
}

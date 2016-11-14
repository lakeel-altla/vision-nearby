package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.domain.repository.FirebasePresenceRepository;
import com.lakeel.profile.notification.data.entity.PresencesEntity;

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

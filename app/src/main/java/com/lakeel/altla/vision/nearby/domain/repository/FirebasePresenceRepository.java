package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.PresencesEntity;

import rx.Single;

public interface FirebasePresenceRepository {

    void savePresenceOnline();

    void savePresenceOfflineOnDisconnected();

    Single<PresencesEntity> findPresenceById(String id);
}

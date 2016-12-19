package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.PresenceEntity;

import rx.Single;

public interface FirebasePresencesRepository {

    void savePresenceOnline(String userId);

    void savePresenceOfflineOnDisconnect(String userId);

    Single<PresenceEntity> findPresenceByUserId(String userId);
}

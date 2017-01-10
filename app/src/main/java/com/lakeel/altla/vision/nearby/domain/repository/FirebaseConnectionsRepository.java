package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.PresenceEntity;

import rx.Completable;
import rx.Single;

public interface FirebaseConnectionsRepository {

    void saveOnline(String userId);

    Completable saveOffline(String userId);

    void savePresenceOfflineOnDisconnect(String userId);

    Single<PresenceEntity> findPresenceByUserId(String userId);
}

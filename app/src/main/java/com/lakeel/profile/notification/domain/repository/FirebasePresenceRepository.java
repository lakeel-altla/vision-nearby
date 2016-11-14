package com.lakeel.profile.notification.domain.repository;

import com.lakeel.profile.notification.data.entity.PresencesEntity;

import rx.Single;

public interface FirebasePresenceRepository {

    void savePresenceOnline();

    void savePresenceOfflineOnDisconnected();

    Single<PresencesEntity> findPresenceById(String id);
}

package com.lakeel.profile.notification.domain.repository;

import com.lakeel.profile.notification.data.entity.RecentlyEntity;

import rx.Observable;
import rx.Single;

public interface FirebaseRecentlyRepository {

    Observable<RecentlyEntity> findRecently();

    Single<RecentlyEntity> findRecentlyByKey(String key);

    Single<Long> findTimes(String id);

    Single<String> saveLocationText(String key, String language, String locationText);
}

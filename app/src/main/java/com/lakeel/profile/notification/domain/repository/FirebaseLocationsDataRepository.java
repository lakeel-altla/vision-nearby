package com.lakeel.profile.notification.domain.repository;

import com.lakeel.profile.notification.data.entity.LocationsDataEntity;

import rx.Single;

public interface FirebaseLocationsDataRepository {

    Single<LocationsDataEntity> findLocationsDataById(String id);
}

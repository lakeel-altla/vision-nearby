package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.PreferenceBeaconIdEntity;
import com.lakeel.altla.vision.nearby.data.entity.PreferenceEntity;

import rx.Single;

public interface PreferenceRepository {

    Single<PreferenceEntity> findPreferences();

    Single<PreferenceBeaconIdEntity> findBeaconId();

    Single<String> saveBeaconId();
}
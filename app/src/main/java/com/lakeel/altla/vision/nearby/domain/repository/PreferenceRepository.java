package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.entity.BeaconIdEntity;
import com.lakeel.altla.vision.nearby.data.entity.PreferencesEntity;
import com.lakeel.altla.vision.nearby.data.entity.SettingsEntity;

import rx.Observable;
import rx.Single;

public interface PreferenceRepository {

    Single<PreferencesEntity> findPreferences();

    Single<BeaconIdEntity> findBeaconId();

    Single<String> saveBeaconId();
}
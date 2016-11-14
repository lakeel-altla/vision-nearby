package com.lakeel.profile.notification.domain.repository;

import com.lakeel.profile.notification.data.entity.BeaconIdEntity;
import com.lakeel.profile.notification.data.entity.PreferencesEntity;
import com.lakeel.profile.notification.data.entity.SettingsEntity;

import rx.Observable;
import rx.Single;

public interface PreferenceRepository {

    Single<PreferencesEntity> findPreferences();

    Single<BeaconIdEntity> findBeaconId();

    Single<String> saveBeaconId();
}
package com.lakeel.altla.vision.nearby.data.repository;

import android.content.SharedPreferences;

import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.data.entity.PreferenceEntity;

import javax.inject.Inject;

import rx.Single;

public class PreferenceRepository {

    private static final String KEY_BEACON_ID = "beaconId/";

    private static final String KEY_ADVERTISE_IN_BACKGROUND = "advertiseInBackground";

    private static final String KEY_SUBSCRIBE_IN_BACKGROUND = "subscribeInBackground";

    private SharedPreferences preference;

    @Inject
    public PreferenceRepository(SharedPreferences preference) {
        this.preference = preference;
    }

    public Single<PreferenceEntity> findPreferences(String userId) {
        String beaconId = preference.getString(KEY_BEACON_ID + userId, StringUtils.EMPTY);
        boolean isAdvertiseInBackground = preference.getBoolean(KEY_ADVERTISE_IN_BACKGROUND, true);
        boolean isSubscribeInBackground = preference.getBoolean(KEY_SUBSCRIBE_IN_BACKGROUND, true);

        PreferenceEntity entity = new PreferenceEntity();
        entity.isAdvertiseInBackgroundEnabled = isAdvertiseInBackground;
        entity.isSubscribeInBackgroundEnabled = isSubscribeInBackground;
        entity.beaconId = beaconId;

        return Single.just(entity);
    }

    public Single<Boolean> findAdvertiseSettings() {
        boolean isSubscribeInBackgroundEnabled = preference.getBoolean(KEY_ADVERTISE_IN_BACKGROUND, true);
        return Single.just(isSubscribeInBackgroundEnabled);
    }

    public Single<Boolean> findSubscribeSettings() {
        boolean isSubscribeInBackgroundEnabled = preference.getBoolean(KEY_SUBSCRIBE_IN_BACKGROUND, true);
        return Single.just(isSubscribeInBackgroundEnabled);
    }

    public Single<String> findBeaconId(String userId) {
        String beaconId = preference.getString(KEY_BEACON_ID + userId, StringUtils.EMPTY);
        if (StringUtils.isEmpty(beaconId)) {
            return Single.just(null);
        } else {
            return Single.just(beaconId);
        }
    }

    public Single<String> saveBeaconId(String userId, String beaconId) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(KEY_BEACON_ID + userId, beaconId);
        editor.apply();
        return Single.just(beaconId);
    }
}

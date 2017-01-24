package com.lakeel.altla.vision.nearby.data.repository;

import android.content.SharedPreferences;

import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.domain.entity.PreferenceEntity;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;

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
        return Single.create(subscriber -> {
            String beaconId = preference.getString(KEY_BEACON_ID + userId, StringUtils.EMPTY);
            boolean isAdvertiseInBackground = preference.getBoolean(KEY_ADVERTISE_IN_BACKGROUND, true);
            boolean isSubscribeInBackground = preference.getBoolean(KEY_SUBSCRIBE_IN_BACKGROUND, true);

            PreferenceEntity entity = new PreferenceEntity();
            entity.isAdvertiseInBackgroundEnabled = isAdvertiseInBackground;
            entity.isSubscribeInBackgroundEnabled = isSubscribeInBackground;
            entity.beaconId = beaconId;

            subscriber.onSuccess(entity);
        });
    }

    public Single<Boolean> findAdvertiseSettings() {
        return Single.create(subscriber -> {
            boolean isSubscribeInBackgroundEnabled = preference.getBoolean(KEY_ADVERTISE_IN_BACKGROUND, true);
            subscriber.onSuccess(isSubscribeInBackgroundEnabled);
        });
    }

    public Single<Boolean> findSubscribeSettings() {
        return Single.create(subscriber -> {
            boolean isSubscribeInBackgroundEnabled = preference.getBoolean(KEY_SUBSCRIBE_IN_BACKGROUND, true);
            subscriber.onSuccess(isSubscribeInBackgroundEnabled);
        });
    }

    public Single<String> findBeaconId(String userId) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                String beaconId = preference.getString(KEY_BEACON_ID + userId, StringUtils.EMPTY);

                if (StringUtils.isEmpty(beaconId)) {
                    subscriber.onSuccess(null);
                    return;
                }

                subscriber.onSuccess(beaconId);
            }
        });
    }

    public Single<String> saveBeaconId(String userId, String beaconId) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                SharedPreferences.Editor editor = preference.edit();
                editor.putString(KEY_BEACON_ID + userId, beaconId);
                editor.apply();
                subscriber.onSuccess(beaconId);
            }
        });
    }
}

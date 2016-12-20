package com.lakeel.altla.vision.nearby.data.repository;

import android.content.SharedPreferences;

import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.data.entity.PreferenceEntity;
import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;

import java.util.UUID;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;

public class PreferenceRepositoryImpl implements PreferenceRepository {

    private static final String KEY_BEACON_ID = "beaconId/";

    private static final String KEY_ADVERTISE_IN_BACKGROUND = "advertiseInBackground";

    private static final String KEY_SUBSCRIBE_IN_BACKGROUND = "subscribeInBackground";

    private SharedPreferences preference;

    @Inject
    public PreferenceRepositoryImpl(SharedPreferences preference) {
        this.preference = preference;
    }

    @Override
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

    @Override
    public Single<Boolean> findSubscribeSetting() {
        return Single.create(subscriber -> {
            boolean isSubscribeInBackgroundEnabled = preference.getBoolean(KEY_SUBSCRIBE_IN_BACKGROUND, true);
            subscriber.onSuccess(isSubscribeInBackgroundEnabled);
        });
    }

    @Override
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

    @Override
    public Single<String> saveBeaconId(String userId) {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                SharedPreferences.Editor editor = preference.edit();

                String uuid = UUID.randomUUID().toString();
                String replacedString = uuid.replace("-", "");
                // Remove 5 - 10 bytes.
                String namespaceId = replacedString.substring(0, 8) + replacedString.substring(20, 32);
                String instanceId = "000000000001";

                editor.putString(KEY_BEACON_ID + userId, namespaceId + instanceId);
                editor.commit();

                String beaconId = namespaceId + instanceId;

                subscriber.onSuccess(beaconId);
            }
        });
    }
}

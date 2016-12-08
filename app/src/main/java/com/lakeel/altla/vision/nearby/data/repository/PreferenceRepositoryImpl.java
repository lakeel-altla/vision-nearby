package com.lakeel.altla.vision.nearby.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.data.entity.PreferenceBeaconIdEntity;
import com.lakeel.altla.vision.nearby.data.entity.PreferenceEntity;
import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;

import java.util.UUID;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;

public class PreferenceRepositoryImpl implements PreferenceRepository {

    private static final String KEY_NAMESPACE_ID = "namespaceId";

    private static final String KEY_INSTANCE_ID = "instanceId";

    private static final String KEY_ADVERTISE_IN_BACKGROUND = "advertiseInBackground";

    private static final String KEY_SUBSCRIBE_IN_BACKGROUND = "subscribeInBackground";

    private SharedPreferences preference;

    @Inject
    public PreferenceRepositoryImpl(SharedPreferences preference) {
        this.preference = preference;
    }

    @Override
    public Single<PreferenceEntity> findPreferences() {
        return Single.create(subscriber -> {
            String namespaceId = preference.getString(KEY_NAMESPACE_ID, StringUtils.EMPTY);
            String instanceId = preference.getString(KEY_INSTANCE_ID, StringUtils.EMPTY);
            boolean isAdvertiseInBackground = preference.getBoolean(KEY_ADVERTISE_IN_BACKGROUND, true);
            boolean isSubscribeInBackground = preference.getBoolean(KEY_SUBSCRIBE_IN_BACKGROUND, true);

            PreferenceEntity entity = new PreferenceEntity();
            entity.isAdvertiseInBackgroundEnabled = isAdvertiseInBackground;
            entity.isSubscribeInBackgroundEnabled = isSubscribeInBackground;
            entity.namespaceId = namespaceId;
            entity.instanceId = instanceId;

            subscriber.onSuccess(entity);
        });
    }

    @Override
    public Single<PreferenceBeaconIdEntity> findBeaconId() {
        return Single.create(new Single.OnSubscribe<PreferenceBeaconIdEntity>() {
            @Override
            public void call(SingleSubscriber<? super PreferenceBeaconIdEntity> subscriber) {
                String namespaceId = preference.getString(KEY_NAMESPACE_ID, StringUtils.EMPTY);
                String instanceId = preference.getString(KEY_INSTANCE_ID, StringUtils.EMPTY);

                if (StringUtils.isEmpty(namespaceId) || StringUtils.isEmpty(instanceId)) {
                    subscriber.onSuccess(null);
                    return;
                }

                PreferenceBeaconIdEntity entity = new PreferenceBeaconIdEntity();
                entity.namespaceId = namespaceId;
                entity.instanceId = instanceId;

                subscriber.onSuccess(entity);
            }
        });
    }

    @Override
    public Single<String> saveBeaconId() {
        return Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
                SharedPreferences.Editor editor = preference.edit();

                String uuid = UUID.randomUUID().toString();
                String replacedString = uuid.replace("-", "");
                // Remove 5 - 10 bytes.
                String namespaceId = replacedString.substring(0, 8) + replacedString.substring(20, 32);
                String instanceId = "000000000001";

                editor.putString(KEY_NAMESPACE_ID, namespaceId);
                editor.putString(KEY_INSTANCE_ID, instanceId);
                editor.commit();

                String beaconId = namespaceId + instanceId;

                subscriber.onSuccess(beaconId);
            }
        });
    }
}

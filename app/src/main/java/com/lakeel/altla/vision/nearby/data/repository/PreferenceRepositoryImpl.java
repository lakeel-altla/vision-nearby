package com.lakeel.altla.vision.nearby.data.repository;

import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.data.entity.BeaconIdEntity;
import com.lakeel.altla.vision.nearby.data.entity.PreferencesEntity;
import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.UUID;

import javax.inject.Inject;

import rx.Single;
import rx.SingleSubscriber;

public class PreferenceRepositoryImpl implements PreferenceRepository {

    private static final String KEY_NAMESPACE_ID = "namespaceId";

    private static final String KEY_INSTANCE_ID = "instanceId";

    private static final String KEY_PUBLISH_IN_BACKGROUND = "publishInBackground";

    private static final String KEY_SUBSCRIBE_IN_BACKGROUND = "subscribeInBackground";

    private SharedPreferences mPreferences;

    @Inject
    PreferenceRepositoryImpl(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public Single<PreferencesEntity> findPreferences() {
        return Single.create(subscriber -> {
            boolean isPublishInBackground = mPreferences.getBoolean(KEY_PUBLISH_IN_BACKGROUND, true);
            boolean isSubscribeInBackground = mPreferences.getBoolean(KEY_SUBSCRIBE_IN_BACKGROUND, true);
            String namespaceId = mPreferences.getString(KEY_NAMESPACE_ID, StringUtils.EMPTY);
            String instanceId = mPreferences.getString(KEY_INSTANCE_ID, StringUtils.EMPTY);

            PreferencesEntity entity = new PreferencesEntity();
            entity.isPublishInBackgroundEnabled = isPublishInBackground;
            entity.isSubscribeInBackgroundEnabled = isSubscribeInBackground;
            entity.namespaceId = namespaceId;
            entity.instanceId = instanceId;

            subscriber.onSuccess(entity);
        });
    }

    @Override
    public Single<BeaconIdEntity> findBeaconId() {
        return Single.create(new Single.OnSubscribe<BeaconIdEntity>() {
            @Override
            public void call(SingleSubscriber<? super BeaconIdEntity> subscriber) {
                String namespaceId = mPreferences.getString(KEY_NAMESPACE_ID, StringUtils.EMPTY);
                String instanceId = mPreferences.getString(KEY_INSTANCE_ID, StringUtils.EMPTY);

                if (StringUtils.isEmpty(namespaceId) || StringUtils.isEmpty(instanceId)) {
                    subscriber.onSuccess(null);
                    return;
                }

                BeaconIdEntity entity = new BeaconIdEntity();
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
                SharedPreferences.Editor editor = mPreferences.edit();

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

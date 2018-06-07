package com.lakeel.altla.vision.nearby.data.repository.android;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.domain.model.Preference;

import javax.inject.Inject;

import rx.Observable;
import rx.Single;

public class PreferenceRepository {

    private static final String KEY_BEACON_ID = "beaconId/";

    private static final String KEY_ADVERTISE_IN_BACKGROUND_ENABLED = "isAdvertiseInBackgroundEnabled";

    private static final String KEY_SUBSCRIBE_IN_BACKGROUND_ENABLED = "isSubscribeInBackgroundEnabled";

    private final SharedPreferences sharedPreferences;

    @Inject
    public PreferenceRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public Single<Preference> findPreferences(@NonNull String userId) {
        Preference preference = new Preference();
        preference.isAdvertiseInBackgroundEnabled = sharedPreferences.getBoolean(KEY_ADVERTISE_IN_BACKGROUND_ENABLED, true);
        preference.isSubscribeInBackgroundEnabled = sharedPreferences.getBoolean(KEY_SUBSCRIBE_IN_BACKGROUND_ENABLED, true);
        preference.beaconId = sharedPreferences.getString(KEY_BEACON_ID + userId, StringUtils.EMPTY);
        return Single.just(preference);
    }

    public Observable<String> saveBeaconId(@NonNull String userId, @NonNull String beaconId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_BEACON_ID + userId, beaconId);
        editor.apply();
        return Observable.just(beaconId);
    }
}

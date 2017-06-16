package com.lakeel.altla.vision.nearby.domain.usecase;

import android.os.Build;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lakeel.altla.vision.nearby.beacon.EddystoneUid;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.data.repository.android.PreferenceRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.BeaconRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserDeviceTokenRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.domain.model.Beacon;
import com.lakeel.altla.vision.nearby.domain.model.DeviceToken;
import com.lakeel.altla.vision.nearby.domain.model.Preference;
import com.lakeel.altla.vision.nearby.domain.model.UserProfile;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class SignInUseCase {

    @Inject
    UserProfileRepository userProfileRepository;

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    UserDeviceTokenRepository userDeviceTokenRepository;

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    SignInUseCase() {
    }

    public Observable<DeviceToken> execute() {
        String userId = CurrentUser.getUid();
        return saveUser(userId)
                .flatMap(aVoid -> findPreferences(userId))
                .filter(preference -> StringUtils.isEmpty(preference.beaconId))
                .flatMap(preference -> {
                    EddystoneUid eddystoneUid = new EddystoneUid();
                    String beaconId = eddystoneUid.getBeaconId();
                    return saveBeaconId(userId, beaconId);
                })
                .flatMap(beaconId -> saveUserBeacon(userId, beaconId))
                .flatMap(beaconId -> saveBeacon(userId, beaconId))
                .flatMap(beacon -> saveDeviceToken(beacon.beaconId));
    }

    private Observable<UserProfile> saveUser(String userId) {
        FirebaseUser firebaseUser = CurrentUser.getUser();

        UserProfile userProfile = new UserProfile();
        userProfile.userId = userId;
        userProfile.name = firebaseUser.getDisplayName();
        userProfile.email = firebaseUser.getEmail();
        if (firebaseUser.getPhotoUrl() != null) {
            userProfile.imageUri = firebaseUser.getPhotoUrl().toString();
        }

        return userProfileRepository.save(userProfile).subscribeOn(Schedulers.io());
    }

    private Observable<Preference> findPreferences(String userId) {
        return preferenceRepository.findPreferences(userId).subscribeOn(Schedulers.io()).toObservable();
    }

    private Observable<String> saveBeaconId(String userId, String beaconId) {
        return preferenceRepository.saveBeaconId(userId, beaconId).subscribeOn(Schedulers.io());
    }

    private Observable<String> saveUserBeacon(String userId, String beaconId) {
        return userProfileRepository.saveUserBeacon(userId, beaconId).subscribeOn(Schedulers.io());
    }

    private Observable<Beacon> saveBeacon(String userId, String beaconId) {
        Beacon beacon = new Beacon();
        beacon.beaconId = beaconId;
        beacon.userId = userId;
        beacon.name = Build.MODEL;
        beacon.os = "android";
        beacon.version = Build.VERSION.RELEASE;
        beacon.lastUsedTime = ServerValue.TIMESTAMP;
        return beaconRepository.save(beacon).subscribeOn(Schedulers.io());
    }

    private Observable<DeviceToken> saveDeviceToken(String beaconId) {
        DeviceToken deviceToken = new DeviceToken();
        deviceToken.userId = CurrentUser.getUid();
        deviceToken.beaconId = beaconId;
        deviceToken.token = FirebaseInstanceId.getInstance().getToken();
        return userDeviceTokenRepository.save(deviceToken).subscribeOn(Schedulers.io());
    }
}

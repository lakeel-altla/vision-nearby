package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.android.PreferenceRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.BeaconRepository;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

public final class SaveLastUsedDeviceTimeUseCase {

    @Inject
    PreferenceRepository preferenceRepository;

    @Inject
    BeaconRepository beaconRepository;

    @Inject
    SaveLastUsedDeviceTimeUseCase() {
    }

    public Observable<Void> execute() {
        String userId = CurrentUser.getUid();

        return preferenceRepository
                .findPreferences(userId)
                .subscribeOn(Schedulers.io())
                .flatMapObservable(preference -> saveLastUsedDeviceTime(preference.beaconId));
    }

    private Observable<Void> saveLastUsedDeviceTime(String beaconId) {
        return beaconRepository.saveLastUsedDeviceTime(beaconId).subscribeOn(Schedulers.io()).toObservable();
    }
}

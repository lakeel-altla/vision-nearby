package com.lakeel.altla.vision.nearby.domain.usecase;

import android.location.Location;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLocationsRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveDeviceLocationUseCase {

    @Inject
    FirebaseLocationsRepository repository;

    @Inject
    SaveDeviceLocationUseCase() {
    }

    public Single<String> execute(Location location) {
        return repository.saveLocation(location).subscribeOn(Schedulers.io());
    }
}

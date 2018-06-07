package com.lakeel.altla.vision.nearby.domain.usecase;

import android.location.Location;
import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.LocationRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveDeviceLocationUseCase {

    @Inject
    LocationRepository repository;

    @Inject
    SaveDeviceLocationUseCase() {
    }

    public Single<String> execute(@NonNull Location location) {
        return repository.save(location).subscribeOn(Schedulers.io());
    }
}

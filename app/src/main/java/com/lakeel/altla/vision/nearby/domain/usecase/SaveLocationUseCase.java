package com.lakeel.altla.vision.nearby.domain.usecase;

import android.location.Location;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLocationsRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveLocationUseCase {

    @Inject
    FirebaseLocationsRepository repository;

    @Inject
    SaveLocationUseCase() {
    }

    public Single<String> execute(Location location) {
        return repository.saveLocation(location);
    }
}

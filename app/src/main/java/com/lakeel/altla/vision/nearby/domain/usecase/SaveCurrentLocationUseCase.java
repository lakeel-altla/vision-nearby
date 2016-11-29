package com.lakeel.altla.vision.nearby.domain.usecase;

import android.location.Location;

import com.lakeel.altla.vision.nearby.data.entity.RecentlyEntity;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseRecentlyRepository;

import javax.inject.Inject;

import rx.Single;

public final class SaveCurrentLocationUseCase {

    @Inject
    FirebaseRecentlyRepository repository;

    @Inject
    SaveCurrentLocationUseCase() {
    }

    public Single<RecentlyEntity> execute(String uniqueId, Location location) {
        return repository.saveCurrentLocation(uniqueId, location);
    }
}

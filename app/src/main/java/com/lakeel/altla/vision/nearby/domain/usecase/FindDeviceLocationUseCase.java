package com.lakeel.altla.vision.nearby.domain.usecase;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseLocationsDataRepository;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseLocationsRepository;
import com.lakeel.altla.vision.nearby.data.entity.LocationDataEntity;
import com.lakeel.altla.vision.nearby.data.entity.LocationEntity;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindDeviceLocationUseCase {

    @Inject
    FirebaseLocationsDataRepository locationsDataRepository;

    @Inject
    FirebaseLocationsRepository locationsRepository;

    @Inject
    FindDeviceLocationUseCase() {
    }

    public Single<LocationEntity> execute(String beaconId) {
        return locationsDataRepository.findLocationsDataByBeaconId(beaconId)
                .subscribeOn(Schedulers.io())
                .flatMap(locationDataEntity -> {
                    Single<LocationDataEntity> single = Single.just(locationDataEntity);
                    Single<GeoLocation> single1 = findLocation(locationDataEntity.uniqueId);
                    return Single.zip(single, single1, LocationEntity::new);
                });
    }

    private Single<GeoLocation> findLocation(String uniqueKey) {
        return locationsRepository.findLocationByKey(uniqueKey).subscribeOn(Schedulers.io());
    }
}

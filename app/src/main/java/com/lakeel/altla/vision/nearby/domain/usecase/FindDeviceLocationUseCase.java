package com.lakeel.altla.vision.nearby.domain.usecase;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseUserLocationMetaDataRepository;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseLocationRepository;
import com.lakeel.altla.vision.nearby.domain.model.Location;
import com.lakeel.altla.vision.nearby.domain.model.LocationMetaData;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindDeviceLocationUseCase {

    @Inject
    FirebaseUserLocationMetaDataRepository locationsDataRepository;

    @Inject
    FirebaseLocationRepository locationsRepository;

    @Inject
    FindDeviceLocationUseCase() {
    }

    public Single<Location> execute(String beaconId) {
        return locationsDataRepository.findLocationMetaData(beaconId)
                .subscribeOn(Schedulers.io())
                .flatMap(locationMetaData -> {
                    Single<LocationMetaData> single = Single.just(locationMetaData);
                    Single<GeoLocation> single1 = findLocation(locationMetaData.locationMetaDataId);
                    return Single.zip(single, single1, Location::new);
                });
    }

    private Single<GeoLocation> findLocation(String locationMetaDataId) {
        return locationsRepository.findLocation(locationMetaDataId).subscribeOn(Schedulers.io());
    }
}

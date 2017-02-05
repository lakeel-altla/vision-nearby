package com.lakeel.altla.vision.nearby.domain.usecase;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.data.repository.firebase.LocationRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserLocationMetaDataRepository;
import com.lakeel.altla.vision.nearby.domain.model.Location;
import com.lakeel.altla.vision.nearby.domain.model.LocationMetaData;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindDeviceLocationUseCase {

    @Inject
    UserLocationMetaDataRepository locationsDataRepository;

    @Inject
    LocationRepository locationsRepository;

    @Inject
    FindDeviceLocationUseCase() {
    }

    public Single<Location> execute(String beaconId) {
        String userId = MyUser.getUserId();
        return locationsDataRepository.findLatest(userId, beaconId)
                .subscribeOn(Schedulers.io())
                .flatMap(locationMetaData -> {
                    if (locationMetaData == null) {
                        return Single.just(null);
                    } else {
                        Single<LocationMetaData> single = Single.just(locationMetaData);
                        Single<GeoLocation> single1 = findLocation(locationMetaData.locationMetaDataId);
                        return Single.zip(single, single1, Location::new);
                    }
                });
    }

    private Single<GeoLocation> findLocation(String locationMetaDataId) {
        return locationsRepository.find(locationMetaDataId).subscribeOn(Schedulers.io());
    }
}

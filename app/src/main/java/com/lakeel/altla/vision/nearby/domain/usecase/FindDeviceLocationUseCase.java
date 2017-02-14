package com.lakeel.altla.vision.nearby.domain.usecase;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.data.repository.firebase.LocationRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserLocationMetaDataRepository;
import com.lakeel.altla.vision.nearby.domain.model.Location;
import com.lakeel.altla.vision.nearby.domain.model.LocationMeta;
import com.lakeel.altla.vision.nearby.presentation.firebase.CurrentUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindDeviceLocationUseCase {

    @Inject
    UserLocationMetaDataRepository locationMetaDataRepository;

    @Inject
    LocationRepository locationsRepository;

    @Inject
    FindDeviceLocationUseCase() {
    }

    public Single<Location> execute(String beaconId) {
        String userId = CurrentUser.getUid();

        return locationMetaDataRepository.findLatest(userId, beaconId)
                .subscribeOn(Schedulers.io())
                .flatMap(metaData -> {
                    if (metaData == null) {
                        return Single.just(null);
                    } else {
                        Single<LocationMeta> single = Single.just(metaData);
                        Single<GeoLocation> single1 = findLocation(metaData.locationMetaDataId);
                        return Single.zip(single, single1, Location::new);
                    }
                });
    }

    private Single<GeoLocation> findLocation(String locationMetaDataId) {
        return locationsRepository.find(locationMetaDataId).subscribeOn(Schedulers.io());
    }
}

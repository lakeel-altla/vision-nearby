package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.firebase.geofire.GeoLocation;
import com.lakeel.altla.vision.nearby.data.repository.firebase.LocationRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserLocationMetaDataRepository;
import com.lakeel.altla.vision.nearby.domain.model.Location;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

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

    public Single<Location> execute(@NonNull String beaconId) {
        String userId = CurrentUser.getUid();

        return locationMetaDataRepository
                .findLatest(userId, beaconId)
                .subscribeOn(Schedulers.io())
                .flatMap(locationMetaData -> {
                    if (locationMetaData == null) {
                        return Single.just(null);
                    } else {
                        return Single.zip(Single.just(locationMetaData), findLocation(locationMetaData.locationMetaDataId), Location::new);
                    }
                });
    }

    private Single<GeoLocation> findLocation(String locationMetaDataId) {
        return locationsRepository.find(locationMetaDataId).subscribeOn(Schedulers.io());
    }
}

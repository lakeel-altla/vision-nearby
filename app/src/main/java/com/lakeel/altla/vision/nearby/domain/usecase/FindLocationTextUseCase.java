package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.resource.Geocode;
import com.lakeel.altla.vision.nearby.domain.repository.RestGeocodeRepository;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class FindLocationTextUseCase {

    @Inject
    RestGeocodeRepository mRestGeocodeRepository;

    @Inject
    public FindLocationTextUseCase() {
    }

    public Single<Geocode> execute(String language, String latitude, String longitude) {
        return mRestGeocodeRepository.findLocationByGeocode(language, latitude, longitude);
    }
}

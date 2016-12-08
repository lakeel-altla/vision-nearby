package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.resource.Geocode;
import com.lakeel.altla.vision.nearby.domain.repository.RestGeocodeRepository;

import javax.inject.Inject;

import rx.Single;

public final class FindLocationTextUseCase {

    @Inject
    RestGeocodeRepository repository;

    @Inject
    FindLocationTextUseCase() {
    }

    public Single<Geocode> execute(String language, String latitude, String longitude) {
        return repository.findLocationText(language, latitude, longitude);
    }
}

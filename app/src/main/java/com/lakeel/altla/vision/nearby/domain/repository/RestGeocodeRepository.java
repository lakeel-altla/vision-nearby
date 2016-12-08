package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.resource.Geocode;

import rx.Single;

public interface RestGeocodeRepository {

    Single<Geocode> findLocationText(String language, String latitude, String longitude);
}

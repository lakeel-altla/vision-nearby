package com.lakeel.altla.vision.nearby.domain.repository;

import com.lakeel.altla.vision.nearby.data.resource.Geocode;

import rx.Single;

public interface RestGeocodeRepository {

    Single<Geocode> findLocationByGeocode(String language, String latitude, String longitude);
}

package com.lakeel.profile.notification.domain.repository;

import com.lakeel.profile.notification.data.resource.Geocode;

import rx.Single;

public interface RestGeocodeRepository {

    Single<Geocode> findLocationByGeocode(String language, String latitude, String longitude);
}

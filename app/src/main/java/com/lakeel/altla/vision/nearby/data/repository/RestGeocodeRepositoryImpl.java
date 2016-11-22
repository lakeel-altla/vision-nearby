package com.lakeel.altla.vision.nearby.data.repository;

import com.lakeel.altla.vision.nearby.BuildConfig;
import com.lakeel.altla.vision.nearby.data.resource.Geocode;
import com.lakeel.altla.vision.nearby.data.rest.geocode.api.GeocodeApi;
import com.lakeel.altla.vision.nearby.domain.repository.RestGeocodeRepository;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Retrofit;
import rx.Single;

public final class RestGeocodeRepositoryImpl implements RestGeocodeRepository {

    private String apiKey = BuildConfig.GEO_CODING_API_KEY;

    @Inject
    @Named("geocodeRetrofit")
    public Retrofit mRetrofit;

    @Inject
    public RestGeocodeRepositoryImpl() {
    }

    @Override
    public Single<Geocode> findLocationByGeocode(String language, String latitude, String longitude) {
        return mRetrofit.create(GeocodeApi.class).find(apiKey, language, latitude + "," + longitude);
    }
}
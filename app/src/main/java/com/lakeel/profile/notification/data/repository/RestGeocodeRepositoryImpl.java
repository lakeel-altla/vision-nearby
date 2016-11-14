package com.lakeel.profile.notification.data.repository;

import com.lakeel.profile.notification.BuildConfig;
import com.lakeel.profile.notification.data.resource.Geocode;
import com.lakeel.profile.notification.data.rest.geocode.api.GeocodeApi;
import com.lakeel.profile.notification.domain.repository.RestGeocodeRepository;

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

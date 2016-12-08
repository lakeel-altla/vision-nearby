package com.lakeel.altla.vision.nearby.data.repository;

import com.lakeel.altla.vision.nearby.BuildConfig;
import com.lakeel.altla.vision.nearby.data.resource.Geocode;
import com.lakeel.altla.vision.nearby.data.rest.geocode.api.GeocodeApi;
import com.lakeel.altla.vision.nearby.domain.repository.RestGeocodeRepository;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Single;

public final class RestGeocodeRepositoryImpl implements RestGeocodeRepository {

    @Inject
    Retrofit mRetrofit;

    @Inject
    RestGeocodeRepositoryImpl() {
    }

    @Override
    public Single<Geocode> findLocationText(String language, String latitude, String longitude) {
        return mRetrofit.create(GeocodeApi.class).find(BuildConfig.GEO_CODING_API_KEY, language, latitude + "," + longitude);
    }
}

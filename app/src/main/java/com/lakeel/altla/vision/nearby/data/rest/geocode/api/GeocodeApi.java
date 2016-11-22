package com.lakeel.altla.vision.nearby.data.rest.geocode.api;

import com.lakeel.altla.vision.nearby.data.resource.Geocode;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Single;

public interface GeocodeApi {

    @Headers({"Content-Type: application/json"})
    @GET("json")
    Single<Geocode> find(@Query("key") String apiKey, @Query("language") String language, @Query("latlng") String latlng);
}

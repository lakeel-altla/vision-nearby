package com.lakeel.altla.vision.nearby.presentation.di.module;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseAuthRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseBeaconsRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseCMLinksRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseConfigsRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseConnectionRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseFavoriteRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseItemsRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseLINELinksRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseLocationsDataRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseLocationsRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebasePresenceRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseRecentlyRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.PreferenceRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.RestCmRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.RestGeocodeRepositoryImpl;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseAuthRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseCMLinksRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseConfigsRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseConnectionRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseFavoriteRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseItemsRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLINELinksRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLocationsDataRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLocationsRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebasePresenceRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseRecentlyRepository;
import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;
import com.lakeel.altla.vision.nearby.domain.repository.RestCmRepository;
import com.lakeel.altla.vision.nearby.domain.repository.RestGeocodeRepository;
import com.lakeel.altla.vision.nearby.presentation.di.ActivityScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RepositoryModule {

    @ActivityScope
    @Provides
    FirebaseFavoriteRepository provideFavoritesRepository(@Named("favoritesUrl") String url) {
        return new FirebaseFavoriteRepositoryImpl(url);
    }

    @ActivityScope
    @Provides
    FirebaseRecentlyRepository provideRecentlyRepository(@Named("recentlyUrl") String url) {
        return new FirebaseRecentlyRepositoryImpl(url);
    }

    @ActivityScope
    @Provides
    FirebaseAuthRepository provideAuthRepository(FirebaseAuthRepositoryImpl repository) {
        return repository;
    }

    @ActivityScope
    @Provides
    FirebaseItemsRepository provideItemsRepository(@Named("itemsUrl") String url) {
        return new FirebaseItemsRepositoryImpl(url);
    }

    @ActivityScope
    @Provides
    FirebaseBeaconsRepository provideBeaconsRepository(@Named("beaconsUrl") String url) {
        return new FirebaseBeaconsRepositoryImpl(url);
    }

    @ActivityScope
    @Provides
    FirebasePresenceRepository providePresenceRepository(@Named("presencesUrl") String url) {
        return new FirebasePresenceRepositoryImpl(url);
    }

    @ActivityScope
    @Provides
    FirebaseConfigsRepository provideConfigsRepository(@Named("configsUrl") String url) {
        return new FirebaseConfigsRepositoryImpl(url);
    }

    @ActivityScope
    @Provides
    FirebaseConnectionRepository provideConnectionRepository(@Named("connectionUrl") String url) {
        return new FirebaseConnectionRepositoryImpl(url);
    }

    @ActivityScope
    @Provides
    RestGeocodeRepository provideLocationRepository(RestGeocodeRepositoryImpl repository) {
        return repository;
    }

    @ActivityScope
    @Provides
    PreferenceRepository providePreferenceRepository(PreferenceRepositoryImpl repository) {
        return repository;
    }

    @ActivityScope
    @Provides
    FirebaseCMLinksRepository provideCMLinksRepository(@Named("CMLinksUrl") String url) {
        return new FirebaseCMLinksRepositoryImpl(url);
    }

    @ActivityScope
    @Provides
    FirebaseLINELinksRepository provideLINELinksRepository(@Named("LINELinksUrl") String url) {
        return new FirebaseLINELinksRepositoryImpl(url);
    }

    @ActivityScope
    @Provides
    FirebaseLocationsRepository provideFirebaseLocationsRepository(@Named("locationsUrl") String url) {
        return new FirebaseLocationsRepositoryImpl(url);
    }

    @ActivityScope
    @Provides
    FirebaseLocationsDataRepository provideFirebaseLocationsDataRepository(@Named("locationsDataUrl") String url) {
        return new FirebaseLocationsDataRepositoryImpl(url);
    }

    @ActivityScope
    @Provides
    RestCmRepository provideCmApiRepository(RestCmRepositoryImpl repository) {
        return repository;
    }

    @ActivityScope
    @Provides
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient()
                .newBuilder()
                .build();
    }

    @ActivityScope
    @Provides
    @Named("geocodeRetrofit")
    Retrofit provideGeocodeRetrofit(@Named("geocodeBaseUrl") String baseUrl, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}

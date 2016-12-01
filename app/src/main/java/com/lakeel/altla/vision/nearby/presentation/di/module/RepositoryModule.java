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
import com.lakeel.altla.vision.nearby.data.repository.FirebaseTokensRepositoryImpl;
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
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseTokensRepository;
import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;
import com.lakeel.altla.vision.nearby.domain.repository.RestCmRepository;
import com.lakeel.altla.vision.nearby.domain.repository.RestGeocodeRepository;
import com.lakeel.altla.vision.nearby.presentation.di.CustomScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RepositoryModule {

    @CustomScope
    @Provides
    FirebaseFavoriteRepository provideFavoritesRepository(@Named("favoritesUrl") String url) {
        return new FirebaseFavoriteRepositoryImpl(url);
    }

    @CustomScope
    @Provides
    FirebaseRecentlyRepository provideRecentlyRepository(@Named("recentlyUrl") String url) {
        return new FirebaseRecentlyRepositoryImpl(url);
    }

    @CustomScope
    @Provides
    FirebaseAuthRepository provideAuthRepository(FirebaseAuthRepositoryImpl repository) {
        return repository;
    }

    @CustomScope
    @Provides
    FirebaseItemsRepository provideItemsRepository(@Named("itemsUrl") String url) {
        return new FirebaseItemsRepositoryImpl(url);
    }

    @CustomScope
    @Provides
    FirebaseBeaconsRepository provideBeaconsRepository(@Named("beaconsUrl") String url) {
        return new FirebaseBeaconsRepositoryImpl(url);
    }

    @CustomScope
    @Provides
    FirebasePresenceRepository providePresenceRepository(@Named("presencesUrl") String url) {
        return new FirebasePresenceRepositoryImpl(url);
    }

    @CustomScope
    @Provides
    FirebaseConfigsRepository provideConfigsRepository(@Named("configsUrl") String url) {
        return new FirebaseConfigsRepositoryImpl(url);
    }

    @CustomScope
    @Provides
    FirebaseConnectionRepository provideConnectionRepository(@Named("connectionUrl") String url) {
        return new FirebaseConnectionRepositoryImpl(url);
    }

    @CustomScope
    @Provides
    RestGeocodeRepository provideLocationRepository(RestGeocodeRepositoryImpl repository) {
        return repository;
    }

    @CustomScope
    @Provides
    PreferenceRepository providePreferenceRepository(PreferenceRepositoryImpl repository) {
        return repository;
    }

    @CustomScope
    @Provides
    FirebaseCMLinksRepository provideCMLinksRepository(@Named("CMLinksUrl") String url) {
        return new FirebaseCMLinksRepositoryImpl(url);
    }

    @CustomScope
    @Provides
    FirebaseLINELinksRepository provideLINELinksRepository(@Named("LINELinksUrl") String url) {
        return new FirebaseLINELinksRepositoryImpl(url);
    }

    @CustomScope
    @Provides
    FirebaseLocationsRepository provideFirebaseLocationsRepository(@Named("locationsUrl") String url) {
        return new FirebaseLocationsRepositoryImpl(url);
    }

    @CustomScope
    @Provides
    FirebaseLocationsDataRepository provideFirebaseLocationsDataRepository(@Named("locationsDataUrl") String url) {
        return new FirebaseLocationsDataRepositoryImpl(url);
    }

    @CustomScope
    @Provides
    FirebaseTokensRepository provideFirebaseTokensRepository(@Named("tokensUrl") String url) {
        return new FirebaseTokensRepositoryImpl(url);
    }

    @CustomScope
    @Provides
    RestCmRepository provideCmApiRepository(RestCmRepositoryImpl repository) {
        return repository;
    }

    @CustomScope
    @Provides
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient()
                .newBuilder()
                .build();
    }

    @CustomScope
    @Provides
    Retrofit provideGeocodeRetrofit(@Named("geocodeBaseUrl") String baseUrl, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}

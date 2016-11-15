package com.lakeel.profile.notification.presentation.di.module;

import com.lakeel.profile.notification.data.repository.FirebaseAuthRepositoryImpl;
import com.lakeel.profile.notification.data.repository.FirebaseBeaconsRepositoryImpl;
import com.lakeel.profile.notification.data.repository.FirebaseCMLinksRepositoryImpl;
import com.lakeel.profile.notification.data.repository.FirebaseConfigsRepositoryImpl;
import com.lakeel.profile.notification.data.repository.FirebaseConnectionRepositoryImpl;
import com.lakeel.profile.notification.data.repository.FirebaseFavoriteRepositoryImpl;
import com.lakeel.profile.notification.data.repository.FirebaseItemsRepositoryImpl;
import com.lakeel.profile.notification.data.repository.FirebaseLINELinksRepositoryImpl;
import com.lakeel.profile.notification.data.repository.FirebaseLocationsDataRepositoryImpl;
import com.lakeel.profile.notification.data.repository.FirebaseLocationsRepositoryImpl;
import com.lakeel.profile.notification.data.repository.FirebasePresenceRepositoryImpl;
import com.lakeel.profile.notification.data.repository.FirebaseRecentlyRepositoryImpl;
import com.lakeel.profile.notification.data.repository.PreferenceRepositoryImpl;
import com.lakeel.profile.notification.data.repository.RestCmRepositoryImpl;
import com.lakeel.profile.notification.data.repository.RestGeocodeRepositoryImpl;
import com.lakeel.profile.notification.domain.repository.FirebaseAuthRepository;
import com.lakeel.profile.notification.domain.repository.FirebaseBeaconsRepository;
import com.lakeel.profile.notification.domain.repository.FirebaseCMLinksRepository;
import com.lakeel.profile.notification.domain.repository.FirebaseConfigsRepository;
import com.lakeel.profile.notification.domain.repository.FirebaseConnectionRepository;
import com.lakeel.profile.notification.domain.repository.FirebaseFavoriteRepository;
import com.lakeel.profile.notification.domain.repository.FirebaseItemsRepository;
import com.lakeel.profile.notification.domain.repository.FirebaseLINELinksRepository;
import com.lakeel.profile.notification.domain.repository.FirebaseLocationsDataRepository;
import com.lakeel.profile.notification.domain.repository.FirebaseLocationsRepository;
import com.lakeel.profile.notification.domain.repository.FirebasePresenceRepository;
import com.lakeel.profile.notification.domain.repository.FirebaseRecentlyRepository;
import com.lakeel.profile.notification.domain.repository.PreferenceRepository;
import com.lakeel.profile.notification.domain.repository.RestCmRepository;
import com.lakeel.profile.notification.domain.repository.RestGeocodeRepository;
import com.lakeel.profile.notification.presentation.di.ActivityScope;

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

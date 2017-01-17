package com.lakeel.altla.vision.nearby.presentation.di.module;

import android.content.Context;
import android.preference.PreferenceManager;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseBeaconsRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseConnectionRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseConnectionsRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseFavoritesRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseHistoryRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseInformationRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseLineLinksRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseLocationsDataRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseLocationsRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseNotificationsRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseTokensRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseUsersRepositoryImpl;
import com.lakeel.altla.vision.nearby.data.repository.PreferenceRepositoryImpl;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseBeaconsRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseConnectionRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseConnectionsRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseFavoritesRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseHistoryRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseInformationRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLineLinksRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLocationsDataRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseLocationsRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseNotificationsRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseTokensRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebaseUsersRepository;
import com.lakeel.altla.vision.nearby.domain.repository.PreferenceRepository;
import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class RepositoryModule {

    @InjectScope
    @Provides
    FirebaseFavoritesRepository provideFavoritesRepository(@Named("favoritesUrl") String url) {
        return new FirebaseFavoritesRepositoryImpl(url);
    }

    @InjectScope
    @Provides
    FirebaseHistoryRepository provideRecentlyRepository(@Named("historyUrl") String url) {
        return new FirebaseHistoryRepositoryImpl(url);
    }

    @InjectScope
    @Provides
    FirebaseUsersRepository provideUsersRepository(@Named("usersUrl") String url) {
        return new FirebaseUsersRepositoryImpl(url);
    }

    @InjectScope
    @Provides
    FirebaseBeaconsRepository provideBeaconsRepository(@Named("beaconsUrl") String url) {
        return new FirebaseBeaconsRepositoryImpl(url);
    }

    @InjectScope
    @Provides
    FirebaseConnectionsRepository providePresenceRepository(@Named("connectionsUrl") String url) {
        return new FirebaseConnectionsRepositoryImpl(url);
    }

    @InjectScope
    @Provides
    FirebaseConnectionRepository provideConnectionRepository(@Named("connectionUrl") String url) {
        return new FirebaseConnectionRepositoryImpl(url);
    }

    @InjectScope
    @Provides
    PreferenceRepository providePreferenceRepository(Context context) {
        return new PreferenceRepositoryImpl(PreferenceManager.getDefaultSharedPreferences(context));
    }

    @InjectScope
    @Provides
    FirebaseLineLinksRepository provideLineLinksRepository(@Named("LineLinksUrl") String url) {
        return new FirebaseLineLinksRepositoryImpl(url);
    }

    @InjectScope
    @Provides
    FirebaseLocationsRepository provideFirebaseLocationsRepository(@Named("locationsUrl") String url) {
        return new FirebaseLocationsRepositoryImpl(url);
    }

    @InjectScope
    @Provides
    FirebaseLocationsDataRepository provideFirebaseLocationsDataRepository(@Named("locationDataUrl") String url) {
        return new FirebaseLocationsDataRepositoryImpl(url);
    }

    @InjectScope
    @Provides
    FirebaseTokensRepository provideFirebaseTokensRepository(@Named("tokensUrl") String url) {
        return new FirebaseTokensRepositoryImpl(url);
    }

    @InjectScope
    @Provides
    FirebaseNotificationsRepository provideFirebaseNotificationRepository(@Named("notificationsUrl") String url) {
        return new FirebaseNotificationsRepositoryImpl(url);
    }

    @InjectScope
    @Provides
    FirebaseInformationRepository provideFirebaseInformationRepository(@Named("informationUrl") String url) {
        return new FirebaseInformationRepositoryImpl(url);
    }

    @InjectScope
    @Provides
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient()
                .newBuilder()
                .build();
    }
}

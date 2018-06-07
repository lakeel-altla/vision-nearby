package com.lakeel.altla.vision.nearby.presentation.di.module;

import android.content.Context;
import android.support.v7.preference.PreferenceManager;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.data.repository.android.PreferenceRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.BeaconRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.ConnectedRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.LINELinksRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.LocationRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.NotificationRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserConnectionRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserDeviceTokenRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserFavoriteRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserInformationRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserLocationMetaDataRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserNearbyHistoryRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserProfileRepository;
import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @InjectScope
    @Provides
    PreferenceRepository providePreferenceRepository(Context context) {
        return new PreferenceRepository(PreferenceManager.getDefaultSharedPreferences(context));
    }

    @InjectScope
    @Provides
    BeaconRepository provideBeaconRepository(Context context) {
        return new BeaconRepository(context.getString(R.string.firebase_database_url) + "/beacons");
    }

    @InjectScope
    @Provides
    ConnectedRepository provideConnectedRepository(Context context) {
        return new ConnectedRepository(context.getString(R.string.firebase_database_url) + "/.info/connected");
    }

    @InjectScope
    @Provides
    LINELinksRepository provideLINELinksRepository(Context context) {
        return new LINELinksRepository(context.getString(R.string.firebase_database_url) + "/link/line");
    }

    @InjectScope
    @Provides
    LocationRepository provideLocationRepository(Context context) {
        return new LocationRepository(context.getString(R.string.firebase_database_url) + "/locations");
    }

    @InjectScope
    @Provides
    NotificationRepository provideNotificationRepository(Context context) {
        return new NotificationRepository(context.getString(R.string.firebase_database_url) + "/notifications");
    }

    @InjectScope
    @Provides
    UserConnectionRepository provideUserConnectionRepository(Context context) {
        return new UserConnectionRepository(context.getString(R.string.firebase_database_url) + "/userConnections");
    }

    @InjectScope
    @Provides
    UserDeviceTokenRepository provideUserDeviceTokenRepository(Context context) {
        return new UserDeviceTokenRepository(context.getString(R.string.firebase_database_url) + "/userDeviceTokens");
    }

    @InjectScope
    @Provides
    UserFavoriteRepository provideUserFavoriteRepository(Context context) {
        return new UserFavoriteRepository(context.getString(R.string.firebase_database_url) + "/userFavorites");
    }

    @InjectScope
    @Provides
    UserInformationRepository provideUserInformationRepository(Context context) {
        return new UserInformationRepository(context.getString(R.string.firebase_database_url) + "/userInformation");
    }

    @InjectScope
    @Provides
    UserLocationMetaDataRepository provideUserLocationMetaDataRepository(Context context) {
        return new UserLocationMetaDataRepository(context.getString(R.string.firebase_database_url) + "/userLocationMetaData");
    }

    @InjectScope
    @Provides
    UserNearbyHistoryRepository provideUserNearbyHistoryRepository(Context context) {
        return new UserNearbyHistoryRepository(context.getString(R.string.firebase_database_url) + "/userNearbyHistory");
    }

    @InjectScope
    @Provides
    UserProfileRepository provideUserProfileRepository(Context context) {
        return new UserProfileRepository(context.getString(R.string.firebase_database_url) + "/userProfiles");
    }
}

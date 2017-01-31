package com.lakeel.altla.vision.nearby.presentation.di.module;

import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public final class ConfigModule {

    @InjectScope
    @Provides
    @Named("userFavoriteUrl")
    String provideFavoritesUrl() {
        return "https://profile-notification-95441.firebaseio.com/userFavorite";
    }

    @InjectScope
    @Provides
    @Named("userNearbyHistoryUrl")
    String provideRecentlyUrl() {
        return "https://profile-notification-95441.firebaseio.com/userNearbyHistory";
    }

    @InjectScope
    @Provides
    @Named("userProfileUrl")
    String provideUsersUrl() {
        return "https://profile-notification-95441.firebaseio.com/userProfile";
    }

    @InjectScope
    @Provides
    @Named("beaconUrl")
    String provideBeaconsUrl() {
        return "https://profile-notification-95441.firebaseio.com/beacon";
    }

    @InjectScope
    @Provides
    @Named("userConnectionUrl")
    String providePresencesUrl() {
        return "https://profile-notification-95441.firebaseio.com/userConnection";
    }

    @InjectScope
    @Provides
    @Named("connectedUrl")
    String provideConnectionUrl() {
        return "https://profile-notification-95441.firebaseio.com/.info/connected";
    }

    @InjectScope
    @Provides
    @Named("lineLinkUrl")
    String provideLineLinksUrl() {
        return "https://profile-notification-95441.firebaseio.com/link/line";
    }

    @InjectScope
    @Provides
    @Named("locationUrl")
    String provideLocationsUrl() {
        return "https://profile-notification-95441.firebaseio.com/location";
    }

    @InjectScope
    @Provides
    @Named("userLocationMetaDataUrl")
    String provideLocationDataUrl() {
        return "https://profile-notification-95441.firebaseio.com/userLocationMetaData";
    }

    @InjectScope
    @Provides
    @Named("userDeviceTokenUrl")
    String provideTokensUrl() {
        return "https://profile-notification-95441.firebaseio.com/userDeviceToken";
    }

    @InjectScope
    @Provides
    @Named("notificationUrl")
    String provideNotificationsUrl() {
        return "https://profile-notification-95441.firebaseio.com/notification";
    }

    @InjectScope
    @Provides
    @Named("userInformationUrl")
    String provideInformationUrl() {
        return "https://profile-notification-95441.firebaseio.com/userInformation";
    }
}

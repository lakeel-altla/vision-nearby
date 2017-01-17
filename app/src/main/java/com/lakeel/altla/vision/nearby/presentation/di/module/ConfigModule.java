package com.lakeel.altla.vision.nearby.presentation.di.module;

import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public final class ConfigModule {

    @InjectScope
    @Provides
    @Named("favoritesUrl")
    String provideFavoritesUrl() {
        return "https://profile-notification-95441.firebaseio.com/favorites";
    }

    @InjectScope
    @Provides
    @Named("historyUrl")
    String provideRecentlyUrl() {
        return "https://profile-notification-95441.firebaseio.com/history";
    }

    @InjectScope
    @Provides
    @Named("usersUrl")
    String provideUsersUrl() {
        return "https://profile-notification-95441.firebaseio.com/users";
    }

    @InjectScope
    @Provides
    @Named("beaconsUrl")
    String provideBeaconsUrl() {
        return "https://profile-notification-95441.firebaseio.com/beacons";
    }

    @InjectScope
    @Provides
    @Named("connectionsUrl")
    String providePresencesUrl() {
        return "https://profile-notification-95441.firebaseio.com/connections";
    }

    @InjectScope
    @Provides
    @Named("profilesUrl")
    String provideProfilesUrl() {
        return "https://profile-notification-95441.firebaseio.com/profiles";
    }

    @InjectScope
    @Provides
    @Named("configsUrl")
    String provideConfigsUrl() {
        return "https://profile-notification-95441.firebaseio.com/configs";
    }

    @InjectScope
    @Provides
    @Named("connectionUrl")
    String provideConnectionUrl() {
        return "https://profile-notification-95441.firebaseio.com/.info/connected";
    }

    @InjectScope
    @Provides
    @Named("cmLinksUrl")
    String provideCmLinksUrl() {
        return "https://profile-notification-95441.firebaseio.com/links/cm";
    }


    @InjectScope
    @Provides
    @Named("LineLinksUrl")
    String provideLineLinksUrl() {
        return "https://profile-notification-95441.firebaseio.com/links/line";
    }

    @InjectScope
    @Provides
    @Named("locationsUrl")
    String provideLocationsUrl() {
        return "https://profile-notification-95441.firebaseio.com/locations";
    }

    @InjectScope
    @Provides
    @Named("locationDataUrl")
    String provideLocationDataUrl() {
        return "https://profile-notification-95441.firebaseio.com/locationData";
    }

    @InjectScope
    @Provides
    @Named("tokensUrl")
    String provideTokensUrl() {
        return "https://profile-notification-95441.firebaseio.com/tokens";
    }

    @InjectScope
    @Provides
    @Named("notificationsUrl")
    String provideNotificationsUrl() {
        return "https://profile-notification-95441.firebaseio.com/notifications";
    }

    @InjectScope
    @Provides
    @Named("informationUrl")
    String provideInformationUrl() {
        return "https://profile-notification-95441.firebaseio.com/information";
    }

    @InjectScope
    @Provides
    @Named("geocodeBaseUrl")
    String provideGeoCodeBaseUrl() {
        return "https://maps.googleapis.com/maps/api/geocode/";
    }
}

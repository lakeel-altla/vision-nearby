package com.lakeel.altla.vision.nearby.presentation.di.module;

import com.lakeel.altla.cm.config.AccessConfig;
import com.lakeel.altla.vision.nearby.presentation.di.CustomScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public final class ConfigModule {

    @CustomScope
    @Provides
    @Named("favoritesUrl")
    String provideUsersUrl() {
        return "https://profile-notification-95441.firebaseio.com/favorites";
    }

    @CustomScope
    @Provides
    @Named("recentlyUrl")
    String provideRecentlyUrl() {
        return "https://profile-notification-95441.firebaseio.com/recently";
    }

    @CustomScope
    @Provides
    @Named("itemsUrl")
    String provideItemsUrl() {
        return "https://profile-notification-95441.firebaseio.com/items";
    }

    @CustomScope
    @Provides
    @Named("beaconsUrl")
    String provideBeaconsUrl() {
        return "https://profile-notification-95441.firebaseio.com/beacons";
    }

    @CustomScope
    @Provides
    @Named("presencesUrl")
    String providePresencesUrl() {
        return "https://profile-notification-95441.firebaseio.com/presences";
    }

    @CustomScope
    @Provides
    @Named("profilesUrl")
    String provideProfilesUrl() {
        return "https://profile-notification-95441.firebaseio.com/profiles";
    }

    @CustomScope
    @Provides
    @Named("configsUrl")
    String provideConfigsUrl() {
        return "https://profile-notification-95441.firebaseio.com/configs";
    }

    @CustomScope
    @Provides
    @Named("connectionUrl")
    String provideConnectionUrl() {
        return "https://profile-notification-95441.firebaseio.com/.info/connected";
    }

    @CustomScope
    @Provides
    @Named("CMLinksUrl")
    String provideCMLinksUrl() {
        return "https://profile-notification-95441.firebaseio.com/links/cm";
    }


    @CustomScope
    @Provides
    @Named("LINELinksUrl")
    String provideLINELinksUrl() {
        return "https://profile-notification-95441.firebaseio.com/links/line";
    }

    @CustomScope
    @Provides
    @Named("locationsUrl")
    String provideLocationsUrl() {
        return "https://profile-notification-95441.firebaseio.com/locations";
    }

    @CustomScope
    @Provides
    @Named("locationsDataUrl")
    String provideLocationsDataUrl() {
        return "https://profile-notification-95441.firebaseio.com/locations-data";
    }

    @CustomScope
    @Provides
    @Named("geocodeBaseUrl")
    String provideGeoCodeBaseUrl() {
        return "https://maps.googleapis.com/maps/api/geocode/";
    }

    @CustomScope
    @Provides
    @Named("CMHost")
    String provideCMHost() {
        return "172.16.10.237";
    }

    @CustomScope
    @Provides
    @Named("CMPort")
    int provideCMPort() {
        return 9080;
    }

    @CustomScope
    @Provides
    AccessConfig provideAccessConfig(@Named("CMHost") String host, @Named("CMPort") int port) {
        return new AccessConfig(host, port);
    }
}

package com.lakeel.profile.notification.presentation.di.module;

import com.lakeel.profile.notification.presentation.di.ActivityScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public final class ConfigModule {

    @ActivityScope
    @Provides
    @Named("favoritesUrl")
    String provideUsersUrl() {
        return "https://profile-notification-95441.firebaseio.com/favorites";
    }

    @ActivityScope
    @Provides
    @Named("recentlyUrl")
    String provideRecentlyUrl() {
        return "https://profile-notification-95441.firebaseio.com/recently";
    }

    @ActivityScope
    @Provides
    @Named("itemsUrl")
    String provideItemsUrl() {
        return "https://profile-notification-95441.firebaseio.com/items";
    }

    @ActivityScope
    @Provides
    @Named("beaconsUrl")
    String provideBeaconsUrl() {
        return "https://profile-notification-95441.firebaseio.com/beacons";
    }

    @ActivityScope
    @Provides
    @Named("presencesUrl")
    String providePresencesUrl() {
        return "https://profile-notification-95441.firebaseio.com/presences";
    }

    @ActivityScope
    @Provides
    @Named("profilesUrl")
    String provideProfilesUrl() {
        return "https://profile-notification-95441.firebaseio.com/profiles";
    }

    @ActivityScope
    @Provides
    @Named("configsUrl")
    String provideConfigsUrl() {
        return "https://profile-notification-95441.firebaseio.com/configs";
    }

    @ActivityScope
    @Provides
    @Named("connectionUrl")
    String provideConnectionUrl() {
        return "https://profile-notification-95441.firebaseio.com/.info/connected";
    }

    @ActivityScope
    @Provides
    @Named("CMLinksUrl")
    String provideCMLinksUrl() {
        return "https://profile-notification-95441.firebaseio.com/links/cm";
    }


    @ActivityScope
    @Provides
    @Named("LINELinksUrl")
    String provideLINELinksUrl() {
        return "https://profile-notification-95441.firebaseio.com/links/line";
    }

    @ActivityScope
    @Provides
    @Named("locationsUrl")
    String provideLocationsUrl() {
        return "https://profile-notification-95441.firebaseio.com/locations";
    }

    @ActivityScope
    @Provides
    @Named("locationsDataUrl")
    String provideLocationsDataUrl() {
        return "https://profile-notification-95441.firebaseio.com/locations-data";
    }

    @ActivityScope
    @Provides
    @Named("geocodeBaseUrl")
    String provideGeoCodeBaseUrl() {
        return "https://maps.googleapis.com/maps/api/geocode/";
    }

    @ActivityScope
    @Provides
    @Named("CMHost")
    String provideCMHost() {
        return "172.16.10.237";
    }

    @ActivityScope
    @Provides
    @Named("CMPort")
    int provideCMPort() {
        return 9080;
    }
}

package com.lakeel.altla.vision.nearby.presentation.di.module;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.lakeel.altla.vision.nearby.presentation.di.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(@NonNull Activity activity) {
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    Activity provideActivity() {
        return activity;
    }

    @ActivityScope
    @Provides
    GoogleApiClient provideGoogleApiClient() {
        return new GoogleApiClient.Builder(activity)
                .addApi(Nearby.MESSAGES_API)
                .build();
    }
}

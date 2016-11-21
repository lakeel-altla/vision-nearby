package com.lakeel.profile.notification.presentation.di.module;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;

import com.lakeel.profile.notification.presentation.di.ActivityScope;

import android.app.Activity;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(@NonNull Activity activity) {
        mActivity = activity;
    }

    @ActivityScope
    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @ActivityScope
    @Provides
    GoogleApiClient provideGoogleApiClient() {
        return new GoogleApiClient.Builder(mActivity)
                .addApi(Nearby.MESSAGES_API)
                .build();
    }
}

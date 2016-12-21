package com.lakeel.altla.vision.nearby.presentation.di.module;

import android.app.Activity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;

import dagger.Module;
import dagger.Provides;

@Module
public final class PresenterModule {

    @InjectScope
    @Provides
    FirebaseAnalytics provideFirebaseAnalytics(Activity activity) {
        return FirebaseAnalytics.getInstance(activity.getApplicationContext());
    }
}

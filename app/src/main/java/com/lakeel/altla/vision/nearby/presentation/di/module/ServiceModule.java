package com.lakeel.altla.vision.nearby.presentation.di.module;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    private final FirebaseAnalytics firebaseAnalytics;

    public ServiceModule(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @InjectScope
    @Provides
    FirebaseAnalytics provideFirebaseAnalytics() {
        return firebaseAnalytics;
    }
}

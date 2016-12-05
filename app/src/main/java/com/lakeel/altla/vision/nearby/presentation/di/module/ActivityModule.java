package com.lakeel.altla.vision.nearby.presentation.di.module;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(@NonNull Activity activity) {
        this.activity = activity;
    }

    @InjectScope
    @Provides
    Activity provideActivity() {
        return activity;
    }
}

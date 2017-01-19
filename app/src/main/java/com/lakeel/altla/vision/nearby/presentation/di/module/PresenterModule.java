package com.lakeel.altla.vision.nearby.presentation.di.module;

import android.content.Context;

import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;

import dagger.Module;
import dagger.Provides;

@Module
public final class PresenterModule {

    @InjectScope
    @Provides
    AnalyticsReporter provideAnalyticsReporter(Context context) {
        return new AnalyticsReporter(context);
    }
}

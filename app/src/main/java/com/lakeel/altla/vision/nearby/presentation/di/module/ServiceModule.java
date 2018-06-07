package com.lakeel.altla.vision.nearby.presentation.di.module;

import android.content.Context;

import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    private final Context context;

    private final AnalyticsReporter analyticsReporter;

    public ServiceModule(Context context) {
        this.context = context;
        analyticsReporter = new AnalyticsReporter(context);
    }

    @InjectScope
    @Provides
    Context provideContext() {
        return context;
    }

    @InjectScope
    @Provides
    AnalyticsReporter provideAnalyticsReporter() {
        return analyticsReporter;
    }
}

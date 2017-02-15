package com.lakeel.altla.vision.nearby.presentation.di.module;

import android.content.Context;

import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @InjectScope
    @Provides
    Context provideContext() {
        return context;
    }
}

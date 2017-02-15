package com.lakeel.altla.vision.nearby.presentation.di.module;

import android.content.Context;
import android.support.v7.preference.PreferenceManager;

import com.lakeel.altla.vision.nearby.data.repository.android.PreferenceRepository;
import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @InjectScope
    @Provides
    PreferenceRepository providePreferenceRepository(Context context) {
        return new PreferenceRepository(PreferenceManager.getDefaultSharedPreferences(context));
    }
}

package com.lakeel.altla.vision.nearby.presentation.di.component;

import com.lakeel.altla.vision.nearby.presentation.di.module.ActivityModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.ApplicationModule;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    ViewComponent viewComponent(ActivityModule module);

    Context context();
}


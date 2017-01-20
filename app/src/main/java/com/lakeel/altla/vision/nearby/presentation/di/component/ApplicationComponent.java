package com.lakeel.altla.vision.nearby.presentation.di.component;

import android.content.Context;

import com.lakeel.altla.vision.nearby.presentation.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    ViewComponent viewComponent();

    Context context();
}


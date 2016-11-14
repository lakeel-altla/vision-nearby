package com.lakeel.profile.notification.presentation.di.component;

import com.lakeel.profile.notification.presentation.di.module.ActivityModule;
import com.lakeel.profile.notification.presentation.di.module.ApplicationModule;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    UserComponent userComponent(ActivityModule module);

    Context context();
}


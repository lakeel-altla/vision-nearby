package com.lakeel.altla.vision.nearby.presentation.di.component;

import com.lakeel.altla.vision.nearby.presentation.beacon.BeaconSubscriber;
import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;
import com.lakeel.altla.vision.nearby.presentation.di.module.ConfigModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.RepositoryModule;

import dagger.Component;

@InjectScope
@Component(modules = {RepositoryModule.class, ConfigModule.class})
public interface DefaultComponent {

    void inject(BeaconSubscriber beaconSubscriber);
}

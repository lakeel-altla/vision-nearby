package com.lakeel.altla.vision.nearby.presentation.di.component;

import com.lakeel.altla.vision.nearby.presentation.application.App;
import com.lakeel.altla.vision.nearby.presentation.beacon.BeaconSubscriber;
import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;
import com.lakeel.altla.vision.nearby.presentation.di.module.ContextModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.RepositoryModule;

import dagger.Component;

@InjectScope
@Component(modules = {ContextModule.class, RepositoryModule.class})
public interface DefaultComponent {

    void inject(App app);

    void inject(BeaconSubscriber beaconSubscriber);
}

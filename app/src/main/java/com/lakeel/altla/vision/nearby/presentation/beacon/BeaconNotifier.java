package com.lakeel.altla.vision.nearby.presentation.beacon;

import android.content.Context;

import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

final class BeaconNotifier implements BootstrapNotifier {

    private final Context context;

    private final Region region;

    private final BackgroundBeaconSubscriber subscriber;

    private RegionBootstrap regionBootstrap;

    BeaconNotifier(Context context, Region region) {
        this.context = context;
        this.region = region;
        subscriber = new BackgroundBeaconSubscriber(context);
    }

    void start() {
        regionBootstrap = new RegionBootstrap(this, region);
    }

    void stop() {
        regionBootstrap.removeRegion(region);
    }

    @Override
    public Context getApplicationContext() {
        return context;
    }

    @Override
    public void didEnterRegion(Region region) {
        subscriber.subscribe(region);
    }

    @Override
    public void didExitRegion(Region region) {
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
    }
}

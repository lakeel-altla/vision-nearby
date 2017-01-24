package com.lakeel.altla.vision.nearby.presentation.beacon;

import android.content.Context;

import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class BeaconDetector implements BootstrapNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconDetector.class);

    private final Context context;

    private final Region region;

    private final BeaconSubscriber subscriber;

    private RegionBootstrap regionBootstrap;

    private boolean isAlreadySubscribed;

    BeaconDetector(Context context) {
        this.context = context;
        // All beacons are subscribed.
        this.region = new Region("background-region", null, null, null);
        subscriber = new BeaconSubscriber(context);
    }

    boolean isAlreadySubscribed() {
        return isAlreadySubscribed;
    }

    void start() {
        isAlreadySubscribed = true;

        // Subscribe beacons in the background.
        regionBootstrap = new RegionBootstrap(this, region);
    }

    void stop() {
        regionBootstrap.removeRegion(region);
    }

    @Override
    public void didEnterRegion(Region region) {
        LOGGER.debug("Enter region.");

        // If enter the region of the beacons, start to subscribe.
        subscriber.subscribe(region);
    }

    @Override
    public void didExitRegion(Region region) {
        LOGGER.debug("Exit region.");
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        LOGGER.debug("Region state is changed.");
    }

    @Override
    public Context getApplicationContext() {
        return context;
    }
}
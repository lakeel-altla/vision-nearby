package com.lakeel.altla.vision.nearby.presentation.beacon;

import android.content.Context;
import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.presentation.beacon.region.RegionType;

import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class BeaconDetector implements BootstrapNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconDetector.class);

    private final Context context;

    private final Region region;

    private RegionBootstrap regionBootstrap;

    private boolean isAlreadySubscribed;

    BeaconDetector(@NonNull Context context) {
        this.context = context;
        region = new Region("background-region", null, null, null);
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
        isAlreadySubscribed = false;

        if (regionBootstrap != null) {
            regionBootstrap.removeRegion(region);
        }
    }

    @Override
    public void didEnterRegion(Region region) {
        LOGGER.info("Enter region.");

        BeaconSubscriber subscriber = new BeaconSubscriber(context, RegionType.ENTER);
        subscriber.subscribe(region);
    }

    @Override
    public void didExitRegion(Region region) {
        LOGGER.info("Exit region.");

        BeaconSubscriber subscriber = new BeaconSubscriber(context, RegionType.EXIT);
        subscriber.subscribe(region);
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        LOGGER.info("Region state is changed:state=" + RegionType.toRegionType(i));
    }

    @Override
    public Context getApplicationContext() {
        return context;
    }
}
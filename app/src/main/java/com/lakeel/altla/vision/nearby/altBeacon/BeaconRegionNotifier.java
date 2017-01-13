package com.lakeel.altla.vision.nearby.altBeacon;

import android.content.Context;

import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class BeaconRegionNotifier implements BootstrapNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconRegionNotifier.class);

    private final Context context;

    BeaconRegionNotifier(Context context) {
        this.context = context;
    }

    @Override
    public Context getApplicationContext() {
        return context;
    }

    @Override
    public void didEnterRegion(Region region) {
        LOGGER.debug("Enter region.");

        BeaconSubscriber subscriber = new BeaconSubscriber(context);
        subscriber.subscribe(region);
    }

    @Override
    public void didExitRegion(Region region) {
        LOGGER.debug("Exit region.");
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
    }
}

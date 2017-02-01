package com.lakeel.altla.vision.nearby.presentation.beacon;

import android.content.Context;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.lakeel.altla.vision.nearby.presentation.beacon.region.RegionState;

import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Date;

final class BeaconDetector implements BootstrapNotifier {

    private static final String TAG = BeaconDetector.class.getSimpleName();

    private final Context context;

    private final Region region;

    private RegionBootstrap regionBootstrap;

    private boolean isAlreadySubscribed;

    BeaconDetector(Context context) {
        this.context = context;
        // All beacons are subscribed.
        this.region = new Region("background-region", null, null, null);
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
        Log.i(TAG, "Enter region.");

        FirebaseCrash.log("Enter region:date:" + new Date().toString());
        FirebaseCrash.report(new RuntimeException("Enter region."));

        BeaconSubscriber subscriber = new BeaconSubscriber(context, RegionState.ENTER);
        subscriber.subscribe(region);
    }

    @Override
    public void didExitRegion(Region region) {
        Log.i(TAG, "Exit region.");

        FirebaseCrash.log("Exit region:date:" + new Date().toString());
        FirebaseCrash.report(new RuntimeException("Exit region."));

        BeaconSubscriber subscriber = new BeaconSubscriber(context, RegionState.EXIT);
        subscriber.subscribe(region);
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        Log.d(TAG, "Region state is changed:state=" + RegionState.toRegionState(i));
    }

    @Override
    public Context getApplicationContext() {
        return context;
    }
}
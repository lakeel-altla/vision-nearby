package com.lakeel.altla.vision.nearby.altBeacon;

import android.content.Context;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

public final class ForegroundBeaconManager extends BeaconManager {

    public ForegroundBeaconManager(Context context) {
        super(context);

        // BeaconManager is designed for a singleton class.
        // So, return new instance of the BeaconManager.

        client = this;

        getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
    }
}

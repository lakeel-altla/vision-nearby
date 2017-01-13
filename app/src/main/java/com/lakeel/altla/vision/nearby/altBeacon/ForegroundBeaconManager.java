package com.lakeel.altla.vision.nearby.altBeacon;

import android.content.Context;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

public final class ForegroundBeaconManager extends BeaconManager {

    private static final String I_BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private static final String EDDYSTONE_EID_LAYOUT = "s:0-1=feaa,m:2-2=30,p:3-3:-41,i:4-11";

    public static ForegroundBeaconManager getInstance(Context context) {
        // BeaconManager is designed for singleton class.
        // So, return new instance of the BeaconManager.
        ForegroundBeaconManager beaconManager = new ForegroundBeaconManager(context);

        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(I_BEACON_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(EDDYSTONE_EID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));

        return beaconManager;
    }

    protected ForegroundBeaconManager(Context context) {
        super(context);
    }
}

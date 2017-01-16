package com.lakeel.altla.vision.nearby.presentation.beacon;

import android.content.Context;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

import java.util.UUID;

public final class BackgroundBeaconManager {

    private static final String I_BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private static final String EDDYSTONE_EID_LAYOUT = "s:0-1=feaa,m:2-2=30,p:3-3:-41,i:4-11";

    private static final long BACKGROUND_SCAN_PERIOD = 10000L;

    private static final long BACKGROUND_BETWEEN_SCAN_PERIOD = 300000L;

    private final BeaconNotifier beaconNotifier;

    public BackgroundBeaconManager(Context context) {
        Region region = new Region(UUID.randomUUID().toString(), null, null, null);
        beaconNotifier = new BeaconNotifier(context, region);

        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(context);

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

        beaconManager.setBackgroundScanPeriod(BACKGROUND_SCAN_PERIOD);
        beaconManager.setBackgroundBetweenScanPeriod(BACKGROUND_BETWEEN_SCAN_PERIOD);

        // Save device battery.
        // Simply constructing this class and holding a reference to it in your custom Application class
        // enables auto battery saving of about 60%.
        new BackgroundPowerSaver(context);
    }

    public void startMonitor() {
        beaconNotifier.start();
    }

    public void stopMonitor() {
        beaconNotifier.stop();
    }
}

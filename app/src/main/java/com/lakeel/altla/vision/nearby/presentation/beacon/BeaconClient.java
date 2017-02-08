package com.lakeel.altla.vision.nearby.presentation.beacon;

import android.content.Context;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

public final class BeaconClient {

    private static final String I_BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private static final String EDDYSTONE_EID_LAYOUT = "s:0-1=feaa,m:2-2=30,p:3-3:-41,i:4-11";

    private final BeaconDetector beaconDetector;

    public BeaconClient(Context context) {
        beaconDetector = new BeaconDetector(context);

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

        beaconManager.setForegroundScanPeriod(10000L);
        beaconManager.setForegroundBetweenScanPeriod(10000L);

        beaconManager.setBackgroundBetweenScanPeriod(60000L);

        // Save device battery.
        // Simply constructing this class and holding a reference to it in your custom Application class
        // enables auto battery saving of about 60%.
        new BackgroundPowerSaver(context);
    }

    public void startDetectBeaconsInBackground() {
        if (!beaconDetector.isAlreadySubscribed()) {
            beaconDetector.start();
        }
    }

    public void stopDetectBeaconsInBackground() {
        beaconDetector.stop();
    }
}

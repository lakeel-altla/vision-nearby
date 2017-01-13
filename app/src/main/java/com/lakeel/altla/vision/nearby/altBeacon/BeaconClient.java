package com.lakeel.altla.vision.nearby.altBeacon;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

import java.util.UUID;

public final class BeaconClient implements BeaconConsumer {

    private static final String I_BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private static final String EDDYSTONE_EID_LAYOUT = "s:0-1=feaa,m:2-2=30,p:3-3:-41,i:4-11";

    private static final long BACKGROUND_SCAN_PERIOD = 10000L;

    private static final long BACKGROUND_BETWEEN_SCAN_PERIOD = 300000L;

    private final Context context;

    private BeaconManager beaconManager;

    private final BeaconRegionNotifier beaconRegionNotifier;

    public BeaconClient(Context context) {
        this.context = context;

        Region region = new Region(UUID.randomUUID().toString(), null, null, null);
        beaconRegionNotifier = new BeaconRegionNotifier(context, region);

        beaconManager = BeaconManager.getInstanceForApplication(context);

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
        beaconManager.bind(this);
    }

    public void stopMonitor() {
        beaconRegionNotifier.stop();
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconRegionNotifier.start();
    }

    @Override
    public Context getApplicationContext() {
        return context;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        context.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return context.bindService(intent, serviceConnection, i);
    }
}

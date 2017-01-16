package com.lakeel.altla.vision.nearby.presentation.presenter.estimation;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;

import com.lakeel.altla.library.EddystoneUID;
import com.lakeel.altla.vision.nearby.altBeacon.BeaconRangeNotifier;
import com.lakeel.altla.vision.nearby.altBeacon.ForegroundBeaconManager;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DistanceEstimationView;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

public final class DistanceEstimationPresenter extends BasePresenter<DistanceEstimationView> implements BeaconConsumer {

    private BeaconRangeNotifier notifier = new BeaconRangeNotifier() {
        @Override
        protected void onFound(String beaconId) {
        }

        @Override
        protected void onDistanceChanged(double distance) {
            String meters = String.format(Locale.getDefault(), "%.2f", distance);
            getView().showDistance(meters);
        }
    };

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceEstimationPresenter.class);

    private Context context;

    private BeaconManager beaconManager;

    private List<Region> regions;

    @Inject
    DistanceEstimationPresenter(Context context) {
        this.context = context;
        beaconManager = new ForegroundBeaconManager(context);
        beaconManager.addRangeNotifier(notifier);
    }

    public void onResume() {
        beaconManager.bind(this);
    }

    public void onPause() {
        beaconManager.unbind(this);
    }

    public void setRegions(List<String> beaconIds) {
        List<Region> regions = new ArrayList<>(beaconIds.size());
        for (String beaconId : beaconIds) {
            EddystoneUID eddystoneUID = new EddystoneUID(beaconId);

            Identifier id1 = Identifier.parse(eddystoneUID.getNamespaceId());
            Identifier id2 = Identifier.parse(eddystoneUID.getInstanceId());

            Region region = new Region(UUID.randomUUID().toString(), id1, id2, null);
            regions.add(region);
        }

        this.regions = regions;
    }

    public void subscribe() {
        try {
            for (Region region : regions) {
                beaconManager.startRangingBeaconsInRegion(region);
            }
        } catch (RemoteException e) {
            LOGGER.error("Failed to estimate distance.", e);
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        subscribe();
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

package com.lakeel.altla.vision.nearby.presentation.beacon;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import com.lakeel.altla.vision.nearby.altBeacon.BeaconRangeNotifier;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerDefaultComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.DefaultComponent;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;
import com.lakeel.altla.vision.nearby.presentation.service.HistoryService;
import com.lakeel.altla.vision.nearby.presentation.service.LineService;
import com.lakeel.altla.vision.nearby.presentation.service.LocationService;
import com.lakeel.altla.vision.nearby.presentation.service.NotificationService;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

public final class BeaconSubscriber {

    @Inject
    FindBeaconUseCase findBeaconUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconSubscriber.class);

    private final Context context;

    private final BeaconManager beaconManager;

    BeaconSubscriber(Context context) {
        DefaultComponent component = DaggerDefaultComponent.create();
        component.inject(this);

        this.context = context;
        beaconManager = BeaconManager.getInstanceForApplication(context);
    }

    public void subscribe(Region region) {
        beaconManager.addRangeNotifier(new BeaconRangeNotifier() {

            @Override
            protected void onFound(String beaconId) {
                if (!MyUser.isAuthenticated()) {
                    return;
                }

                findBeaconUseCase
                        .execute(beaconId)
                        .subscribeOn(Schedulers.io())
                        .subscribe(entity -> {
                            if (entity == null) {
                                LOGGER.info("Not registered beacon:beaconId=" + beaconId);
                                return;
                            }

                            Intent historyIntent = new Intent(context, HistoryService.class);
                            historyIntent.putExtra(IntentKey.BEACON_ID.name(), beaconId);
                            context.startService(historyIntent);

                            Intent notificationIntent = new Intent(context, NotificationService.class);
                            notificationIntent.putExtra(IntentKey.BEACON_ID.name(), beaconId);
                            context.startService(notificationIntent);

                            Intent locationIntent = new Intent(context, LocationService.class);
                            locationIntent.putExtra(IntentKey.BEACON_ID.name(), beaconId);
                            context.startService(locationIntent);

                            Intent lineIntent = new Intent(context, LineService.class);
                            lineIntent.putExtra(IntentKey.USER_ID.name(), entity.userId);
                            context.startService(lineIntent);
                        });

                try {
                    // Stop to subscribe.
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    LOGGER.error("Failed to stop to subscribe beacons.", e);
                }
            }
        });

        try {
            // Start to subscribe.
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            LOGGER.error("Failed to start to subscribe beacons.", e);
        }
    }
}

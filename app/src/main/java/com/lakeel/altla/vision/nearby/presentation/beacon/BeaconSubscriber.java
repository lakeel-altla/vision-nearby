package com.lakeel.altla.vision.nearby.presentation.beacon;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import com.google.firebase.auth.FirebaseAuth;
import com.lakeel.altla.vision.nearby.beacon.BeaconRangeNotifier;
import com.lakeel.altla.vision.nearby.domain.model.Beacon;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.presentation.beacon.region.RegionState;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerDefaultComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.DefaultComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ContextModule;
import com.lakeel.altla.vision.nearby.presentation.service.LINEService;
import com.lakeel.altla.vision.nearby.presentation.service.LocationService;
import com.lakeel.altla.vision.nearby.presentation.service.NearbyHistoryService;
import com.lakeel.altla.vision.nearby.presentation.service.NotificationService;
import com.lakeel.altla.vision.nearby.presentation.view.intent.IntentKey;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public final class BeaconSubscriber {

    @Inject
    FindBeaconUseCase findBeaconUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(BeaconSubscriber.class);

    private final Context context;

    private final RegionState regionState;

    private final BeaconManager beaconManager;

    BeaconSubscriber(Context context, RegionState regionState) {
        DefaultComponent component = DaggerDefaultComponent.builder()
                .contextModule(new ContextModule(context))
                .build();
        component.inject(this);

        this.context = context;
        this.regionState = regionState;
        beaconManager = BeaconManager.getInstanceForApplication(context);
    }

    public void subscribe(Region region) {
        beaconManager.addRangeNotifier(new BeaconRangeNotifier() {

            @Override
            protected void onFound(String beaconId) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    return;
                }

                findBeaconUseCase
                        .execute(beaconId)
                        .subscribe(beacon -> {
                            if (beacon == null) {
                                LOGGER.info("Not registered the beacon:beaconId=" + beaconId);
                                return;
                            }

                            LOGGER.info("Found beacon:beaconId=" + beaconId);

                            // Start services in background.
                            startNearbyHistoryService(beacon.userId);
                            startNotificationService(beacon);
                            startLocationService(beacon);
                            startLineService(beacon.userId);
                        });

                try {
                    // Stop token subscribe.
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    LOGGER.error("Failed token stopService token subscribe beacons.", e);
                }
            }
        });

        try {
            // Start token subscribe.
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            LOGGER.error("Failed token start token subscribe beacons.", e);
        }
    }

    private void startNearbyHistoryService(String userId) {
        Intent intent = new Intent(context, NearbyHistoryService.class);
        intent.putExtra(IntentKey.USER_ID.name(), userId);
        intent.putExtra(IntentKey.REGION_STATE.name(), regionState.getValue());
        context.startService(intent);
    }

    private void startNotificationService(Beacon beacon) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra(IntentKey.USER_ID.name(), beacon.userId);
        intent.putExtra(IntentKey.BEACON_ID.name(), beacon.beaconId);
        context.startService(intent);
    }

    private void startLocationService(Beacon beacon) {
        Intent intent = new Intent(context, LocationService.class);
        intent.putExtra(IntentKey.USER_ID.name(), beacon.userId);
        intent.putExtra(IntentKey.BEACON_ID.name(), beacon.beaconId);
        context.startService(intent);
    }

    private void startLineService(String userId) {
        Intent intent = new Intent(context, LINEService.class);
        intent.putExtra(IntentKey.USER_ID.name(), userId);
        context.startService(intent);
    }
}

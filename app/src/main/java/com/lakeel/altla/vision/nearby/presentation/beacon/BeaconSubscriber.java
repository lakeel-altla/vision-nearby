package com.lakeel.altla.vision.nearby.presentation.beacon;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.beacon.BeaconRangeNotifier;
import com.lakeel.altla.vision.nearby.domain.model.Beacon;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.presentation.beacon.region.RegionType;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerDefaultComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.DefaultComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ContextModule;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;
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

    private final RegionType regionType;

    private final BeaconManager beaconManager;

    BeaconSubscriber(@NonNull Context context, @NonNull RegionType regionType) {
        DefaultComponent component = DaggerDefaultComponent.builder()
                .contextModule(new ContextModule(context))
                .build();
        component.inject(this);

        this.context = context;
        this.regionType = regionType;
        beaconManager = BeaconManager.getInstanceForApplication(context);
    }

    public void subscribe(@NonNull Region region) {
        beaconManager.addRangeNotifier(new BeaconRangeNotifier() {

            @Override
            protected void onFound(String beaconId) {
                if (CurrentUser.getUser() == null) {
                    LOGGER.warn("Not subscribe beacons in background because not signed in.");
                    return;
                }

                findBeaconUseCase
                        .execute(beaconId)
                        .subscribe(beacon -> {
                            if (beacon == null) {
                                LOGGER.info("Not registered beacon:beaconId=" + beaconId);
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
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    LOGGER.error("Failed to stop to subscribe beacons.", e);
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            LOGGER.error("Failed to start to subscribe beacons.", e);
        }
    }

    private void startNearbyHistoryService(@NonNull String userId) {
        Intent intent = new Intent(context, NearbyHistoryService.class);
        intent.putExtra(IntentKey.USER_ID.name(), userId);
        intent.putExtra(IntentKey.REGION_TYPE.name(), regionType.getValue());
        context.startService(intent);
    }

    private void startNotificationService(@NonNull Beacon beacon) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra(IntentKey.USER_ID.name(), beacon.userId);
        intent.putExtra(IntentKey.BEACON_ID.name(), beacon.beaconId);
        context.startService(intent);
    }

    private void startLocationService(@NonNull Beacon beacon) {
        Intent intent = new Intent(context, LocationService.class);
        intent.putExtra(IntentKey.USER_ID.name(), beacon.userId);
        intent.putExtra(IntentKey.BEACON_ID.name(), beacon.beaconId);
        context.startService(intent);
    }

    private void startLineService(@NonNull String userId) {
        Intent intent = new Intent(context, LINEService.class);
        intent.putExtra(IntentKey.USER_ID.name(), userId);
        context.startService(intent);
    }
}

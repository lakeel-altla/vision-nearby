package com.lakeel.altla.vision.nearby.presentation.beacon;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;

import com.lakeel.altla.vision.nearby.beacon.BeaconRangeNotifier;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.presentation.beacon.region.RegionState;
import com.lakeel.altla.vision.nearby.presentation.di.component.DaggerDefaultComponent;
import com.lakeel.altla.vision.nearby.presentation.di.component.DefaultComponent;
import com.lakeel.altla.vision.nearby.presentation.di.module.ContextModule;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.service.HistoryService;
import com.lakeel.altla.vision.nearby.presentation.service.LINEService;
import com.lakeel.altla.vision.nearby.presentation.service.LocationService;
import com.lakeel.altla.vision.nearby.presentation.service.NotificationService;
import com.lakeel.altla.vision.nearby.presentation.view.intent.IntentKey;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;

import javax.inject.Inject;

public final class BeaconSubscriber {

    @Inject
    FindBeaconUseCase findBeaconUseCase;

    private static final String TAG = BeaconSubscriber.class.getSimpleName();

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
                if (!MyUser.isAuthenticated()) {
                    return;
                }

                findBeaconUseCase
                        .execute(beaconId)
                        .subscribe(beacon -> {
                            if (beacon == null) {
                                Log.i(TAG, "Not registered the beacon:beaconId=" + beaconId);
                                return;
                            }
                            String userId = MyUser.getUserId();
                            if (userId.equals(beacon.userId)) {
                                Log.i(TAG, "This beacon is mine:beaconId=" + beaconId);
                                return;
                            }

                            Log.i(TAG, "Found beacon:beaconId=" + beaconId);

                            Intent historyIntent = new Intent(context, HistoryService.class);
                            historyIntent.putExtra(IntentKey.USER_ID.name(), beacon.userId);
                            historyIntent.putExtra(IntentKey.REGION.name(), regionState.getValue());
                            context.startService(historyIntent);

                            Intent notificationIntent = new Intent(context, NotificationService.class);
                            notificationIntent.putExtra(IntentKey.USER_ID.name(), beacon.userId);
                            notificationIntent.putExtra(IntentKey.BEACON_ID.name(), beaconId);
                            context.startService(notificationIntent);

                            Intent locationIntent = new Intent(context, LocationService.class);
                            locationIntent.putExtra(IntentKey.USER_ID.name(), beacon.userId);
                            locationIntent.putExtra(IntentKey.BEACON_ID.name(), beaconId);
                            context.startService(locationIntent);

                            Intent lineIntent = new Intent(context, LINEService.class);
                            lineIntent.putExtra(IntentKey.USER_ID.name(), beacon.userId);
                            context.startService(lineIntent);
                        });

                try {
                    // Stop to subscribe.
                    beaconManager.stopRangingBeaconsInRegion(region);
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to stopService to subscribe beacons.", e);
                }
            }
        });

        try {
            // Start to subscribe.
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to start to subscribe beacons.", e);
        }
    }
}

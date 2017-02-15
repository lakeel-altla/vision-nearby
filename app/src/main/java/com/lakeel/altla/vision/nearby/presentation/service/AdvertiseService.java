package com.lakeel.altla.vision.nearby.presentation.service;

import android.app.Service;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.beacon.EddystoneUid;
import com.lakeel.altla.vision.nearby.presentation.view.intent.IntentKey;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class AdvertiseService extends Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertiseService.class);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            throw new RuntimeException("Intent is empty.");
        }

        Bundle bundle = intent.getExtras();
        String beaconId = (String) bundle.get(IntentKey.BEACON_ID.name());

        EddystoneUid eddystoneUID = new EddystoneUid(beaconId);
        String namespaceId = eddystoneUID.getNamespaceId();
        String instanceId = eddystoneUID.getInstanceId();

        LOGGER.debug("namespaceId=" + namespaceId + " instanceId=" + instanceId);

        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT);
        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);

        if (beaconTransmitter.isStarted()) {
            LOGGER.warn("Already started.");
        } else {
            // Transmit as a Eddystone-UID.
            Beacon beacon = new Beacon.Builder()
                    .setId1(namespaceId)
                    .setId2(instanceId)
                    .build();

            beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {
                @Override
                public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                    super.onStartSuccess(settingsInEffect);
                    LOGGER.info("Succeeded token advertise as a beacon.");

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                    builder.setContentTitle(getApplicationContext().getResources().getString(R.string.notification_title_advertise_ble));
                    builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);
                    startForeground(UUID.randomUUID().variant(), builder.build());
                }

                @Override
                public void onStartFailure(int errorCode) {
                    super.onStartFailure(errorCode);
                    LOGGER.error("Failed token start token advertise:errorCode=" + errorCode);
                }
            });
        }

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        LOGGER.info("AdvertiseService is killed because app task is removed by user action.");
    }

    @Override
    public void onDestroy() {
        LOGGER.info("AdvertiseService is destroyed.");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

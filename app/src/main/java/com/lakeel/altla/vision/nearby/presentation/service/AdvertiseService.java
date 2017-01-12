package com.lakeel.altla.vision.nearby.presentation.service;

import android.app.Service;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.lakeel.altla.library.EddystoneUID;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.presentation.intent.IntentKey;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public final class AdvertiseService extends Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdvertiseService.class);

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            String beaconId = (String) bundle.get(IntentKey.BEACON_ID.name());

            EddystoneUID eddystoneUID = new EddystoneUID(beaconId);
            String namespaceId = eddystoneUID.getNamespaceId();
            String instanceId = eddystoneUID.getInstanceId();

            LOGGER.debug("Namespace Id=" + namespaceId + " Instance Id=" + instanceId);

            if (StringUtils.isEmpty(namespaceId) || StringUtils.isEmpty(instanceId)) {
                LOGGER.error("Stop AdvertiseService because Namespace ID or Instance ID was empty.");
                stopSelf();
            } else {
                BeaconParser beaconParser = new BeaconParser()
                        .setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT);

                BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);

                if (beaconTransmitter.isStarted()) {
                    LOGGER.warn("Already started");
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
                            LOGGER.debug("Succeeded to start advertising.");

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                            builder.setContentTitle(getApplicationContext().getResources().getString(R.string.notification_title_advertise_ble));
                            builder.setContentText(getApplicationContext().getResources().getString(R.string.notification_message_advertise_ble));
                            builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm);

                            startForeground(UUID.randomUUID().variant(), builder.build());
                        }

                        @Override
                        public void onStartFailure(int errorCode) {
                            super.onStartFailure(errorCode);
                            LOGGER.error("Failed to start advertising　Error code = " + errorCode);
                        }
                    });
                }
            }
        }

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        LOGGER.info("App task is removed by user action");
    }

    @Override
    public void onDestroy() {
        LOGGER.info("Service is destroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

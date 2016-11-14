package com.lakeel.profile.notification.presentation.service;

import com.lakeel.profile.notification.core.StringUtils;
import com.lakeel.profile.notification.presentation.intent.IntentKey;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Service;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

public final class PublishService extends Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishService.class);

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            String namespaceId = (String) bundle.get(IntentKey.NAMESPACE_ID.name());
            String instanceId = (String) bundle.get(IntentKey.INSTANCE_ID.name());

            LOGGER.debug("namespaceId=" + namespaceId + " instanceId=" + instanceId);

            if (StringUtils.isEmpty(namespaceId) || StringUtils.isEmpty(instanceId)) {
                LOGGER.error("Stop publish service because namespace id or instance id were empty");
                stopSelf();
            } else {
                BeaconParser beaconParser = new BeaconParser()
                        .setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT);

                BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);

                if (beaconTransmitter.isStarted()) {
                    LOGGER.debug("Already started");
                } else {
                    // Transmit as a Eddystone UID.
                    Beacon beacon = new Beacon.Builder()
                            .setId1(namespaceId)
                            .setId2(instanceId)
                            .build();

                    beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {
                        @Override
                        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                            super.onStartSuccess(settingsInEffect);
                            LOGGER.debug("Succeeded to start advertising.");
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

package com.lakeel.profile.notification.presentation.receiver;


import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.BleSignal;
import com.google.android.gms.nearby.messages.Distance;
import com.google.android.gms.nearby.messages.Message;

import com.lakeel.altla.library.AttachmentListener;
import com.lakeel.profile.notification.presentation.constants.AttachmentType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class NearbyReceiver extends BroadcastReceiver {

    // Because BroadcastReceiver is running in background after app instance is removed, can not use dagger.

    private static final Logger LOGGER = LoggerFactory.getLogger(NearbyReceiver.class);

    @Override
    public void onReceive(final Context context, Intent intent) {
        Nearby.Messages.handleIntent(intent, new AttachmentListener() {

                    @Override
                    protected void onFound(String type, String value) {
                        AttachmentType attachmentType = AttachmentType.toType(type);
                        if (attachmentType == AttachmentType.UNKNOWN) {
                            LOGGER.warn("Unhandled type:type=" + type);
                            return;
                        }
                        attachmentType.startService(context, value);
                    }

                    @Override
                    public void onLost(Message message) {
                        LOGGER.debug("Message is Lost");
                    }

                    @Override
                    public void onDistanceChanged(Message message, Distance distance) {
                        LOGGER.debug("Beacon distance is changed:distance=" + distance.getMeters());
                    }

                    @Override
                    public void onBleSignalChanged(Message message, BleSignal bleSignal) {
                        LOGGER.debug("BLE signal is changed");
                    }
                }
        );
    }
}
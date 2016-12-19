package com.lakeel.altla.vision.nearby.presentation.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscribeService extends Service implements GoogleApiClient.ConnectionCallbacks {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeService.class);

    private GoogleApiClient googleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();

        LOGGER.debug("onCreate");

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Nearby.MESSAGES_API)
                .build();

        googleApiClient.registerConnectionCallbacks(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LOGGER.debug("onStartCommand");

        googleApiClient.connect();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LOGGER.debug("onConnected");

        Nearby.Messages.subscribe(googleApiClient, new MessageListener() {

            @Override
            public void onFound(Message message) {
                super.onFound(message);
                LOGGER.debug("onFound");
            }

            @Override
            public void onLost(Message var1) {
                LOGGER.debug("onLost");
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
}

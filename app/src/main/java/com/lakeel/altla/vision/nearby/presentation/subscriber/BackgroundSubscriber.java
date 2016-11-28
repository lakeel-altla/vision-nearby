package com.lakeel.altla.vision.nearby.presentation.subscriber;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.lakeel.altla.library.ResolutionResultCallback;
import com.lakeel.altla.vision.nearby.presentation.receiver.NearbyReceiver;

public final class BackgroundSubscriber implements Subscriber {

    private final GoogleApiClient googleApiClient;

    private final PendingIntent pendingIntent;

    public BackgroundSubscriber(Context context, GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
        pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, NearbyReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void subscribe(ResolutionResultCallback callback) {
        Nearby.Messages.subscribe(googleApiClient, pendingIntent)
                .setResultCallback(callback);
    }

    @Override
    public void unSubscribe(ResolutionResultCallback callback) {
        Nearby.Messages.unsubscribe(googleApiClient, pendingIntent)
                .setResultCallback(callback);
    }
}

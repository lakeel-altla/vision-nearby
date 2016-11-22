package com.lakeel.altla.vision.nearby.presentation.nearby;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;

import com.lakeel.altla.library.ResolutionResultCallback;
import com.lakeel.altla.vision.nearby.presentation.receiver.NearbyReceiver;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public final class BackgroundSubscriber implements Subscriber {

    private final GoogleApiClient mGoogleApiClient;

    private final PendingIntent mPendingIntent;

    public BackgroundSubscriber(Context context, GoogleApiClient googleApiClient) {
        mGoogleApiClient = googleApiClient;
        mPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, NearbyReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void subscribe(ResolutionResultCallback callback) {
        Nearby.Messages.subscribe(mGoogleApiClient, mPendingIntent)
                .setResultCallback(callback);
    }

    @Override
    public void unSubscribe(ResolutionResultCallback callback) {
        Nearby.Messages.unsubscribe(mGoogleApiClient, mPendingIntent)
                .setResultCallback(callback);
    }
}

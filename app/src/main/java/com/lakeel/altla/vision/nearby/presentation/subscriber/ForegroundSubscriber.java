package com.lakeel.altla.vision.nearby.presentation.subscriber;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import com.lakeel.altla.library.ResolutionResultCallback;

import android.support.annotation.NonNull;

public final class ForegroundSubscriber implements Subscriber {

    private final GoogleApiClient mGoogleApiClient;

    private final MessageListener mMessageListener;

    private final SubscribeOptions mOptions;

    public ForegroundSubscriber(@NonNull GoogleApiClient googleApiClient, @NonNull MessageListener messageListener, SubscribeOptions options) {
        mGoogleApiClient = googleApiClient;
        mMessageListener = messageListener;
        mOptions = options;
    }

    @Override
    public void subscribe(ResolutionResultCallback callback) {
        if (mOptions == null) {
            Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener)
                    .setResultCallback(callback);
        } else {
            Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener, mOptions)
                    .setResultCallback(callback);
        }
    }

    @Override
    public void unSubscribe(ResolutionResultCallback callback) {
        Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener)
                .setResultCallback(callback);
    }
}

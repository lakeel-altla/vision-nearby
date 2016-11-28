package com.lakeel.altla.vision.nearby.presentation.subscriber;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.lakeel.altla.library.ResolutionResultCallback;

public final class ForegroundSubscriber implements Subscriber {

    private final GoogleApiClient googleApiClient;

    private final MessageListener messageListener;

    private SubscribeOptions options;

    public ForegroundSubscriber(@NonNull GoogleApiClient googleApiClient, @NonNull MessageListener messageListener) {
        this(googleApiClient, messageListener, null);
    }

    public ForegroundSubscriber(@NonNull GoogleApiClient googleApiClient, @NonNull MessageListener messageListener, SubscribeOptions options) {
        this.googleApiClient = googleApiClient;
        this.messageListener = messageListener;
        this.options = options;
    }

    @Override
    public void subscribe(ResolutionResultCallback callback) {
        if (options == null) {
            Nearby.Messages.subscribe(googleApiClient, messageListener)
                    .setResultCallback(callback);
        } else {
            Nearby.Messages.subscribe(googleApiClient, messageListener, options)
                    .setResultCallback(callback);
        }
    }

    @Override
    public void unSubscribe(ResolutionResultCallback callback) {
        Nearby.Messages.unsubscribe(googleApiClient, messageListener)
                .setResultCallback(callback);
    }
}

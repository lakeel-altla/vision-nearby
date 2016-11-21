package com.lakeel.profile.notification.domain.usecase;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import rx.Completable;

public final class ConnectGoogleApiClientUseCase {

    @Inject
    GoogleApiClient mGoogleApiClient;

    @Inject
    ConnectGoogleApiClientUseCase() {
    }

    public Completable execute() {
        if (mGoogleApiClient.isConnected()) {
            return Completable.complete();
        }

        return Completable.create(subscriber -> {
            mGoogleApiClient.unregisterConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {
                    subscriber.onCompleted();
                    mGoogleApiClient.unregisterConnectionCallbacks(this);
                }

                @Override
                public void onConnectionSuspended(int i) {
                }
            });

            mGoogleApiClient.registerConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    subscriber.onError(new RuntimeException());
                    mGoogleApiClient.unregisterConnectionFailedListener(this);
                }
            });

            mGoogleApiClient.connect();
        });
    }
}

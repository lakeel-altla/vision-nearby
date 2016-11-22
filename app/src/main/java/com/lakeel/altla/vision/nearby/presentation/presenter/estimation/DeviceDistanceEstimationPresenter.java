package com.lakeel.altla.vision.nearby.presentation.presenter.estimation;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Distance;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageFilter;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import com.lakeel.altla.library.EddystoneUID;
import com.lakeel.altla.library.ResolutionResultCallback;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.presentation.nearby.AbstractSubscriber;
import com.lakeel.altla.vision.nearby.presentation.nearby.ForegroundSubscriber;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.DeviceDistanceEstimationView;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Locale;

import javax.inject.Inject;

public final class DeviceDistanceEstimationPresenter extends BasePresenter<DeviceDistanceEstimationView> implements GoogleApiClient.ConnectionCallbacks {

    @Inject
    FindBeaconUseCase mFindBeaconUseCase;

    private final GoogleApiClient mGoogleApiClient;

    private AbstractSubscriber mSubscriber;

    private ResolutionResultCallback mResultCallback = new ResolutionResultCallback() {
        @Override
        protected void onResolution(Status status) {

        }
    };

    private MessageListener mMessageListener = new MessageListener() {
        @Override
        public void onFound(Message message) {
            super.onFound(message);
        }

        @Override
        public void onDistanceChanged(Message message, Distance distance) {
            super.onDistanceChanged(message, distance);
            String meters = String.format(Locale.getDefault(), "%.2f", distance.getMeters());
            getView().showDistance(meters);
        }
    };

    @Inject
    DeviceDistanceEstimationPresenter(Activity activity) {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Nearby.MESSAGES_API)
                .build();
    }

    @Override
    public void onResume() {
        mGoogleApiClient.registerConnectionCallbacks(this);
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        mGoogleApiClient.unregisterConnectionCallbacks(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        mSubscriber.unSubscribe(new ResolutionResultCallback() {
            @Override
            protected void onResolution(Status status) {

            }
        });

        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        onSubscribe();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    public void setSubscriber(String beaconId) {
        EddystoneUID eddystoneUID = new EddystoneUID(beaconId);
        String namespaceId = eddystoneUID.getNamespaceId();
        String instanceId = eddystoneUID.getInstanceId();

        MessageFilter filter = new MessageFilter.Builder()
                .includeEddystoneUids(namespaceId, instanceId)
                .build();
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setFilter(filter)
                .build();

        mSubscriber = new ForegroundSubscriber(mGoogleApiClient, mMessageListener, options);
    }

    public void onSubscribe() {
        mSubscriber.subscribe(new ResolutionResultCallback() {
            @Override
            protected void onResolution(Status status) {

            }
        });
    }
}

package com.lakeel.altla.vision.nearby.presentation.presenter.estimation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

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
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserBeaconsUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.subscriber.ForegroundSubscriber;
import com.lakeel.altla.vision.nearby.presentation.subscriber.Subscriber;
import com.lakeel.altla.vision.nearby.presentation.view.DeviceDistanceEstimationView;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public final class DeviceDistanceEstimationPresenter extends BasePresenter<DeviceDistanceEstimationView> implements GoogleApiClient.ConnectionCallbacks {

    @Inject
    FindUserBeaconsUseCase findUserBeaconsUseCase;

    private final GoogleApiClient googleApiClient;

    private Subscriber subscriber;

    private ResolutionResultCallback resultCallback = new ResolutionResultCallback() {
        @Override
        protected void onResolution(Status status) {
            getView().showResolutionSystemDialog(status);
        }
    };

    private MessageListener messageListener = new MessageListener() {

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
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Nearby.MESSAGES_API)
                .build();
    }

    public void onResume() {
        googleApiClient.registerConnectionCallbacks(this);
        googleApiClient.connect();
    }

    public void onPause() {
        googleApiClient.unregisterConnectionCallbacks(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        subscriber.unSubscribe(resultCallback);

        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        onSubscribe();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    public void setSubscriber(List<String> beaconIds) {
        MessageFilter.Builder filterBuilder = new MessageFilter.Builder();

        for (String beaconId : beaconIds) {
            EddystoneUID eddystoneUID = new EddystoneUID(beaconId);
            String namespaceId = eddystoneUID.getNamespaceId();
            String instanceId = eddystoneUID.getInstanceId();

            filterBuilder.includeEddystoneUids(namespaceId, instanceId);
        }

        SubscribeOptions options = new SubscribeOptions.Builder()
                .setFilter(filterBuilder.build())
                .build();

        subscriber = new ForegroundSubscriber(googleApiClient, messageListener, options);
    }

    public void onSubscribe() {
        subscriber.subscribe(resultCallback);
    }
}

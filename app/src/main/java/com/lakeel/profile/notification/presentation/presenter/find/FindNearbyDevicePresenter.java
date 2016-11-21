package com.lakeel.profile.notification.presentation.presenter.find;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Distance;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageFilter;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import com.lakeel.profile.notification.domain.usecase.FindBeaconUseCase;
import com.lakeel.profile.notification.presentation.parser.EddystoneUID;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
import com.lakeel.profile.notification.presentation.view.FindNearbyDeviceView;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Locale;

import javax.inject.Inject;

public final class FindNearbyDevicePresenter extends BasePresenter<FindNearbyDeviceView> implements GoogleApiClient.ConnectionCallbacks {

    @Inject
    FindBeaconUseCase mFindBeaconUseCase;

    private GoogleApiClient mGoogleApiClient;

    private String mBeaconId;

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
    FindNearbyDevicePresenter(Activity activity) {
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

        Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener);

        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        onSubscribe();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    public void setBeaconId(String beaconId) {
        mBeaconId = beaconId;
    }

    private void onSubscribe() {
        EddystoneUID eddystoneUID = new EddystoneUID(mBeaconId);
        String namespaceId = eddystoneUID.getNamespaceId();
        String instanceId = eddystoneUID.getInstanceId();

        MessageFilter filter = new MessageFilter.Builder()
                .includeEddystoneUids(namespaceId, instanceId)
                .build();
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setFilter(filter)
                .build();

        Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener, options);
    }
}

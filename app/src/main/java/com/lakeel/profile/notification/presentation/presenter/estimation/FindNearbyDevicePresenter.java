package com.lakeel.profile.notification.presentation.presenter.estimation;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class FindNearbyDevicePresenter extends BasePresenter<FindNearbyDeviceView> {

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
    GoogleApiClient mGoogleApiClient;

    @Inject
    FindBeaconUseCase mFindBeaconUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(FindNearbyDevicePresenter.class);

    private String mBeaconId;

    private SubscribeOptions mOptions;

    @Inject
    FindNearbyDevicePresenter() {
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mGoogleApiClient.isConnected()) {
            Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener, mOptions);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener);
    }

    public void setSubscribeOptions(String beaconId) {
        mBeaconId = beaconId;

        EddystoneUID eddystoneUID = new EddystoneUID(beaconId);
        String namespaceId = eddystoneUID.getNamespaceId();
        String instanceId = eddystoneUID.getInstanceId();

        MessageFilter filter = new MessageFilter.Builder()
                .includeEddystoneUids(namespaceId, instanceId)
                .build();
        mOptions = new SubscribeOptions.Builder()
                .setFilter(filter)
                .build();
    }
}

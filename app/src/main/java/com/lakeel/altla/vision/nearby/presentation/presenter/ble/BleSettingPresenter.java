package com.lakeel.altla.vision.nearby.presentation.presenter.ble;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker.State;
import com.lakeel.altla.vision.nearby.presentation.constants.AnalyticsEvent;
import com.lakeel.altla.vision.nearby.presentation.constants.AnalyticsParam;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.service.AdvertiseService;
import com.lakeel.altla.vision.nearby.presentation.service.RunningService;
import com.lakeel.altla.vision.nearby.presentation.view.BleSettingView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class BleSettingPresenter extends BasePresenter<BleSettingView> {

    @Inject
    FirebaseAnalytics firebaseAnalytics;

    @Inject
    FindBeaconIdUseCase findBeaconIdUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(BleSettingPresenter.class);

    private Context context;

    @Inject
    BleSettingPresenter(Context context) {
        this.context = context;
    }

    public void onActivityCreated() {
        BleChecker checker = new BleChecker(context);
        State state = checker.checkState();
        if (state == State.SUBSCRIBE_ONLY) {
            getView().disableAdvertiseSettings();
        }
    }

    public void onStartAdvertise() {
        Bundle bundle = new Bundle();
        bundle.putString(AnalyticsParam.USER_ID.getValue(), MyUser.getUserData().userId);
        bundle.putString(AnalyticsParam.USER_NAME.getValue(), MyUser.getUserData().displayName);
        firebaseAnalytics.logEvent(AnalyticsEvent.ON_ADVERTISE.getValue(), bundle);

        Subscription subscription = findBeaconIdUseCase
                .execute(MyUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beaconId -> getView().startAdvertise(beaconId),
                        e -> LOGGER.error("Failed to find a beacon ID.", e));
        subscriptions.add(subscription);
    }

    public void onStopAdvertise() {
        Bundle bundle = new Bundle();
        bundle.putString(AnalyticsParam.USER_ID.getValue(), MyUser.getUserData().userId);
        bundle.putString(AnalyticsParam.USER_NAME.getValue(), MyUser.getUserData().displayName);
        firebaseAnalytics.logEvent(AnalyticsEvent.OFF_ADVERTISE.getValue(), bundle);

        RunningService runningService = new RunningService(context, AdvertiseService.class);
        runningService.stop();
    }

    public void onStartSubscribe() {
        Bundle bundle = new Bundle();
        bundle.putString(AnalyticsParam.USER_ID.getValue(), MyUser.getUserData().userId);
        bundle.putString(AnalyticsParam.USER_NAME.getValue(), MyUser.getUserData().displayName);
        firebaseAnalytics.logEvent(AnalyticsEvent.ON_SUBSCRIBE.getValue(), bundle);

        getView().startSubscribe();
    }

    public void onStopSubscribe() {
        Bundle bundle = new Bundle();
        bundle.putString(AnalyticsParam.USER_ID.getValue(), MyUser.getUserData().userId);
        bundle.putString(AnalyticsParam.USER_NAME.getValue(), MyUser.getUserData().displayName);
        firebaseAnalytics.logEvent(AnalyticsEvent.OFF_SUBSCRIBE.getValue(), bundle);

        getView().stopSubscribe();
    }
}

package com.lakeel.altla.vision.nearby.presentation.presenter.ble;

import android.content.Context;

import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker.State;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.service.AdvertiseService;
import com.lakeel.altla.vision.nearby.presentation.service.RunningService;
import com.lakeel.altla.vision.nearby.presentation.view.BleSettingsView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class BleSettingsPresenter extends BasePresenter<BleSettingsView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindBeaconIdUseCase findBeaconIdUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(BleSettingsPresenter.class);

    private Context context;

    @Inject
    BleSettingsPresenter(Context context) {
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
        analyticsReporter.onAdvertise();

        Subscription subscription = findBeaconIdUseCase
                .execute(MyUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beaconId -> getView().startAdvertise(beaconId),
                        e -> LOGGER.error("Failed to find a beacon ID.", e));
        subscriptions.add(subscription);
    }

    public void onStopAdvertise() {
        analyticsReporter.offAdvertise();

        RunningService service = new RunningService(context, AdvertiseService.class);
        service.stop();
    }

    public void onStartSubscribe() {
        analyticsReporter.onSubscribe();
        getView().startSubscribe();
    }

    public void onStopSubscribe() {
        analyticsReporter.offSubscribe();
        getView().stopSubscribe();
    }
}

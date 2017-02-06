package com.lakeel.altla.vision.nearby.presentation.presenter.ble;

import android.content.Context;

import com.lakeel.altla.vision.nearby.domain.usecase.FindPreferenceUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker.State;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.service.AdvertiseService;
import com.lakeel.altla.vision.nearby.presentation.service.RunningServiceManager;
import com.lakeel.altla.vision.nearby.presentation.view.BleSettingsView;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class BleSettingsPresenter extends BasePresenter<BleSettingsView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindPreferenceUseCase findPreferenceUseCase;

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private final Context context;

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

    public void onStop() {
        subscriptions.unSubscribe();
    }

    public void onStartAdvertise() {
        analyticsReporter.onAdvertise();

        Subscription subscription = findPreferenceUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(preference -> getView().startAdvertiseInBackground(preference.beaconId), new ErrorAction<>());
        subscriptions.add(subscription);
    }

    public void onStopAdvertise() {
        analyticsReporter.offAdvertise();

        RunningServiceManager runningServiceManager = new RunningServiceManager(context, AdvertiseService.class);
        runningServiceManager.stopService();
    }

    public void onStartSubscribe() {
        analyticsReporter.onSubscribe();
        getView().startSubscribeInBackground();
    }

    public void onStopSubscribe() {
        analyticsReporter.offSubscribe();
        getView().stopSubscribeInBackground();
    }
}

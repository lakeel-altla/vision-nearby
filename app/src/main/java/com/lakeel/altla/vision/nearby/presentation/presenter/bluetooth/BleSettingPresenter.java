package com.lakeel.altla.vision.nearby.presentation.presenter.bluetooth;

import android.content.Context;

import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker.State;
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
        Subscription subscription = findBeaconIdUseCase
                .execute(MyUser.getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beaconId -> getView().startAdvertise(beaconId),
                        e -> LOGGER.error("Failed to find a beacon ID.", e));
        subscriptions.add(subscription);
    }

    public void onStopAdvertise() {
        RunningService runningService = new RunningService(context, AdvertiseService.class);
        runningService.stop();
    }
}

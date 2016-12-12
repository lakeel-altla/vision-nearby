package com.lakeel.altla.vision.nearby.presentation.presenter.settings.bluetooth;

import android.content.Context;

import com.lakeel.altla.vision.nearby.domain.usecase.FindPreferenceBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.presentation.checker.BluetoothChecker;
import com.lakeel.altla.vision.nearby.presentation.checker.BluetoothChecker.BleState;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.BeaconIdModelMapper;
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
    FindPreferenceBeaconIdUseCase findPreferenceBeaconIdUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(BleSettingsPresenter.class);

    private BeaconIdModelMapper beaconIdModelMapper = new BeaconIdModelMapper();

    private Context context;

    @Inject
    BleSettingsPresenter(Context context) {
        this.context = context;
    }

    public void onActivityCreated() {
        BluetoothChecker checker = new BluetoothChecker(context);
        BleState state = checker.getState();
        if (state == BleState.SUBSCRIBE_ONLY) {
            getView().disableAdvertiseSettings();
        }
    }

    public void onStartAdvertise() {
        Subscription subscription = findPreferenceBeaconIdUseCase
                .execute()
                .map(entity -> beaconIdModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().startAdvertise(model),
                        e -> LOGGER.error("Failed to find a beacon ID.", e));
        subscriptions.add(subscription);
    }

    public void onStopAdvertise() {
        RunningService runningService = new RunningService(context, AdvertiseService.class);
        runningService.stop();
    }
}

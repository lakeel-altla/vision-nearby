package com.lakeel.altla.vision.nearby.presentation.presenter.settings.bluetooth;

import android.content.Context;

import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconIdUseCase;
import com.lakeel.altla.vision.nearby.presentation.checker.BluetoothChecker;
import com.lakeel.altla.vision.nearby.presentation.checker.BluetoothChecker.BleState;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.BeaconIdModelMapper;
import com.lakeel.altla.vision.nearby.presentation.service.PublishService;
import com.lakeel.altla.vision.nearby.presentation.service.ServiceManager;
import com.lakeel.altla.vision.nearby.presentation.view.BleSettingsView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class BleSettingsPresenter extends BasePresenter<BleSettingsView> {

    @Inject
    FindBeaconIdUseCase mFindBeaconIdUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(BleSettingsPresenter.class);

    private BeaconIdModelMapper mBeaconIdModelMapper = new BeaconIdModelMapper();

    private Context mContext;

    @Inject
    BleSettingsPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void onResume() {
        BluetoothChecker checker = new BluetoothChecker(mContext);
        BleState state = checker.getState();
        if (state == BleState.SUBSCRIBE_ONLY) {
            getView().disablePublishSettings();
        }
    }

    public void onStartPublish() {
        Subscription subscription = mFindBeaconIdUseCase
                .execute()
                .map(entity -> mBeaconIdModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().startPublish(model),
                        e -> LOGGER.error("Failed to find beacon id", e));

        mCompositeSubscription.add(subscription);
    }

    public void onStopPublishing() {
        ServiceManager manager = new ServiceManager(mContext, PublishService.class);
        manager.stopService();
    }
}

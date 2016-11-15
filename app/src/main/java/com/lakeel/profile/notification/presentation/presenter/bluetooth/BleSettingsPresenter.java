package com.lakeel.profile.notification.presentation.presenter.bluetooth;

import com.lakeel.profile.notification.domain.usecase.FindBeaconIdUseCase;
import com.lakeel.profile.notification.presentation.checker.BleState;
import com.lakeel.profile.notification.presentation.checker.BluetoothChecker;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
import com.lakeel.profile.notification.presentation.presenter.mapper.BeaconIdModelMapper;
import com.lakeel.profile.notification.presentation.service.PublishService;
import com.lakeel.profile.notification.presentation.view.BleSettingsView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.ACTIVITY_SERVICE;

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

    public void onStartToPublish() {
        Subscription subscription = mFindBeaconIdUseCase
                .execute()
                .map(entity -> mBeaconIdModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().startPublishInService(model),
                        e -> LOGGER.error("Failed to find beacon id", e));
        mCompositeSubscription.add(subscription);
    }

    public void onStopToPublish() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> listServiceInfo = am.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : listServiceInfo) {
            if (runningServiceInfo.service.getClassName().equals(PublishService.class.getName())) {
                Intent intent = new Intent(mContext, PublishService.class);
                mContext.stopService(intent);
            }
        }
    }
}

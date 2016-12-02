package com.lakeel.altla.vision.nearby.presentation.presenter.settings.device;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserBeaconsUseCase;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.BeaconModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.BeaconModel;
import com.lakeel.altla.vision.nearby.presentation.view.DeviceItemView;
import com.lakeel.altla.vision.nearby.presentation.view.DeviceView;
import com.lakeel.altla.vision.nearby.presentation.view.adapter.DeviceAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.schedulers.Schedulers;

public class DeviceListPresenter extends BasePresenter<DeviceView> {

    @Inject
    FindUserUseCase mFindUserUseCase;

    @Inject
    FindUserBeaconsUseCase findUserBeaconsUseCase;

    @Inject
    FindBeaconsUseCase findBeaconsUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceListPresenter.class);

    private final BeaconModelMapper mMapper = new BeaconModelMapper();

    private List<BeaconModel> mModels = new ArrayList<>();

    @Inject
    DeviceListPresenter() {
    }

    @Override
    public void onResume() {
        Subscription subscription = findUserBeaconsUseCase
                .execute(MyUser.getUid())
                .flatMap(beaconId -> findBeaconsUseCase.execute(beaconId).subscribeOn(Schedulers.io()).toObservable())
                .filter(entity -> entity != null)
                .map(mMapper::map)
                .toList()
                .subscribeOn(Schedulers.io())
                .subscribe(models -> {
                    mModels.clear();
                    mModels.addAll(models);
                    getView().updateItems();
                }, e -> {
                    LOGGER.error("Failed to find user beacons.", e);
                });
        reusableCompositeSubscription.add(subscription);
    }

    public int getItemCount() {
        return mModels.size();
    }

    public void onCreateItemView(DeviceAdapter.DeviceViewHolder viewHolder) {
        DeviceListPresenter.DeviceItemPresenter itemPresenter = new DeviceListPresenter.DeviceItemPresenter();
        itemPresenter.onCreateItemView(viewHolder);
        viewHolder.setItemPresenter(itemPresenter);
    }

    public class DeviceItemPresenter extends BaseItemPresenter<DeviceItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(mModels.get(position));
        }

        public void onClick(BeaconModel model) {
            getView().showTrackingFragment(model.mBeaconId, model.mName);
        }
    }
}

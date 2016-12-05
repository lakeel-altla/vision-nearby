package com.lakeel.altla.vision.nearby.presentation.presenter.settings.device;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.data.entity.BeaconEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserBeaconsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
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

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class DeviceListPresenter extends BasePresenter<DeviceView> {

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindUserBeaconsUseCase findUserBeaconsUseCase;

    @Inject
    FindBeaconUseCase findBeaconUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceListPresenter.class);

    private final BeaconModelMapper modelMapper = new BeaconModelMapper();

    private List<BeaconModel> itemModels = new ArrayList<>();

    @Inject
    DeviceListPresenter() {
    }

    @Override
    public void onResume() {
        Subscription subscription = findUserBeaconsUseCase
                .execute(MyUser.getUid())
                .flatMap(this::findBeacon)
                .filter(entity -> entity != null)
                .map(modelMapper::map)
                .toList()
                .subscribeOn(Schedulers.io())
                .subscribe(models -> {
                    itemModels.clear();
                    itemModels.addAll(models);
                    getView().updateItems();
                }, e -> {
                    LOGGER.error("Failed to find user beacons.", e);
                });
        reusableCompositeSubscription.add(subscription);
    }

    public int getItemCount() {
        return itemModels.size();
    }

    public void onCreateItemView(DeviceAdapter.DeviceViewHolder viewHolder) {
        DeviceListPresenter.DeviceItemPresenter itemPresenter = new DeviceListPresenter.DeviceItemPresenter();
        itemPresenter.onCreateItemView(viewHolder);
        viewHolder.setItemPresenter(itemPresenter);
    }

    public class DeviceItemPresenter extends BaseItemPresenter<DeviceItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(itemModels.get(position));
        }

        public void onClick(BeaconModel model) {
            getView().showTrackingFragment(model.mBeaconId, model.mName);
        }
    }

    Observable<BeaconEntity> findBeacon(String beaconId) {
        return findBeaconUseCase.execute(beaconId).subscribeOn(Schedulers.io()).toObservable();
    }
}

package com.lakeel.profile.notification.presentation.presenter.device;

import com.lakeel.profile.notification.data.entity.ItemsEntity;
import com.lakeel.profile.notification.domain.usecase.FindBeaconUseCase;
import com.lakeel.profile.notification.domain.usecase.FindItemUseCase;
import com.lakeel.profile.notification.presentation.firebase.MyUser;
import com.lakeel.profile.notification.presentation.presenter.BaseItemPresenter;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
import com.lakeel.profile.notification.presentation.presenter.mapper.DeviceModelMapper;
import com.lakeel.profile.notification.presentation.presenter.model.DeviceModel;
import com.lakeel.profile.notification.presentation.view.DeviceItemView;
import com.lakeel.profile.notification.presentation.view.DeviceView;
import com.lakeel.profile.notification.presentation.view.adapter.DeviceAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DeviceListPresenter extends BasePresenter<DeviceView> {

    @Inject
    FindItemUseCase mFindItemUseCase;

    @Inject
    FindBeaconUseCase mFindBeaconUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceListPresenter.class);

    private final DeviceModelMapper mMapper = new DeviceModelMapper();

    private List<DeviceModel> mModels = new ArrayList<>();

    @Inject
    DeviceListPresenter() {
    }

    @Override
    public void onResume() {
        Subscription subscription = mFindItemUseCase
                .execute(MyUser.getUid())
                .toObservable()
                .filter(entity -> !entity.beacons.isEmpty())
                .flatMap(entity -> Observable.just(entity.beacons.entrySet()))
                .flatMapIterable(entries -> entries)
                .flatMap(new Func1<Map.Entry<String, ItemsEntity.UserBeaconEntity>, Observable<DeviceModel>>() {
                    @Override
                    public Observable<DeviceModel> call(Map.Entry<String, ItemsEntity.UserBeaconEntity> entry) {
                        return mFindBeaconUseCase
                                .execute(entry.getKey())
                                .map(mMapper::map)
                                .subscribeOn(Schedulers.io())
                                .toObservable();
                    }
                })
                .toList()
                .subscribe(models -> {
                    mModels.clear();
                    mModels.addAll(models);
                    getView().updateItems();
                }, e -> {
                    LOGGER.error("Failed to find user beacons.", e);
                });

        mCompositeSubscription.add(subscription);
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

        public void onClick(DeviceModel model) {
            getView().showTrackingFragment(model.mBeaconId, model.mName);
        }
    }
}

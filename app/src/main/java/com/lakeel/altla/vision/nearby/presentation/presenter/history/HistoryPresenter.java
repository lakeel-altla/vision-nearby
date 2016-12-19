package com.lakeel.altla.vision.nearby.presentation.presenter.history;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.CollectionUtils;
import com.lakeel.altla.vision.nearby.data.entity.HistoryEntity;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindHistoryUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.RemoveHistoryUseCase;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.HistoryModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.HistoryModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel;
import com.lakeel.altla.vision.nearby.presentation.view.HistoryItemView;
import com.lakeel.altla.vision.nearby.presentation.view.HistoryView;
import com.lakeel.altla.vision.nearby.presentation.view.bundle.HistoryBundle;
import com.lakeel.altla.vision.nearby.presentation.view.bundle.WeatherBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class HistoryPresenter extends BasePresenter<HistoryView> {

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindHistoryUseCase findHistoryUseCase;

    @Inject
    RemoveHistoryUseCase removeHistoryUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryPresenter.class);

    private HistoryModelMapper historyModelMapper = new HistoryModelMapper();

    private final List<HistoryModel> historyModels = new ArrayList<>();

    public List<HistoryModel> getItems() {
        return historyModels;
    }

    @Inject
    HistoryPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findHistoryUseCase
                .execute(MyUser.getUid())
                .flatMap(entity -> {
                    Observable<HistoryEntity> historyObservable = Observable.just(entity);
                    Observable<UserEntity> userObservable = findUser(entity.userId);
                    return Observable.zip(historyObservable, userObservable, (historyEntity, userEntity) ->
                            historyModelMapper.map(historyEntity, userEntity));
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(historyItemModels -> {
                    Collections.reverse(historyItemModels);
                    historyModels.clear();
                    historyModels.addAll(historyItemModels);
                    getView().updateItems();
                }, e -> {
                    LOGGER.error("Failed to find history.", e);
                    getView().showSnackBar(R.string.error_process);
                });
        subscriptions.add(subscription);
    }

    public void onCreateItemView(HistoryItemView historyItemView) {
        HistoryItemPresenter historyItemPresenter = new HistoryItemPresenter();
        historyItemPresenter.onCreateItemView(historyItemView);
        historyItemView.setItemPresenter(historyItemPresenter);
    }

    public final class HistoryItemPresenter extends BaseItemPresenter<HistoryItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(historyModels.get(position));
        }

        public void onClick(HistoryModel model) {
            HistoryBundle data = new HistoryBundle();

            data.userId = model.userId;
            data.userName = model.name;

            LocationModel locationModel = model.locationModel;
            if (locationModel != null) {
                data.latitude = locationModel.latitude;
                data.longitude = locationModel.longitude;
            }

            if (model.detectedActivity != null) {
                data.detectedActivity = model.detectedActivity;
            }

            if (model.weather != null) {
                WeatherBundle weatherBundle = new WeatherBundle();
                weatherBundle.conditions = model.weather.conditions;
                weatherBundle.humidity = model.weather.humidity;
                weatherBundle.temperature = model.weather.temperature;
                data.weatherBundle = weatherBundle;
            }

            data.timestamp = model.passingTime;

            getView().showHistoryFragment(data);
        }

        public void onRemove(HistoryModel model) {
            Subscription subscription = removeHistoryUseCase
                    .execute(MyUser.getUid(), model.uniqueId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(e -> {
                        LOGGER.error("Failed to add to favorites.", e);
                        getView().showSnackBar(R.string.error_not_added);
                    }, () -> {
                        int size = historyModels.size();
                        historyModels.remove(model);
                        if (CollectionUtils.isEmpty(historyModels)) {
                            getView().removeAll(size);
                        } else {
                            getView().updateItems();
                        }
                        getView().showSnackBar(R.string.message_removed);
                    });
            subscriptions.add(subscription);
        }
    }

    Observable<UserEntity> findUser(String userId) {
        return findUserUseCase.execute(userId).subscribeOn(Schedulers.io()).toObservable();
    }
}

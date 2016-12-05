package com.lakeel.altla.vision.nearby.presentation.presenter.history;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindRecentlyUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.intent.HistoryBundleData;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.RecentlyModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.HistoryModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel;
import com.lakeel.altla.vision.nearby.presentation.view.HistoryItemView;
import com.lakeel.altla.vision.nearby.presentation.view.HistoryListView;

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

public final class HistoryListPresenter extends BasePresenter<HistoryListView> {

    @Inject
    FindUserUseCase mFindUserUseCase;

    @Inject
    FindFavoriteUseCase mFindFavoriteUseCase;

    @Inject
    FindRecentlyUseCase mFindRecentlyUseCase;

    @Inject
    SaveFavoriteUseCase mSaveFavoriteUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryListPresenter.class);

    private RecentlyModelMapper mRecentlyModelMapper = new RecentlyModelMapper();

    private final List<HistoryModel> mHistoryModels = new ArrayList<>();

    public List<HistoryModel> getItems() {
        return mHistoryModels;
    }

    @Inject
    HistoryListPresenter() {
    }

    @Override
    public void onResume() {
        Subscription subscription = mFindRecentlyUseCase
                .execute(MyUser.getUid())
                .flatMap(entity -> {
                    Observable<UserEntity> userObservable = mFindUserUseCase.execute(entity.userId).subscribeOn(Schedulers.io()).toObservable();
                    return Observable.zip(Observable.just(entity), userObservable, (historyEntity, userEntity) ->
                            mRecentlyModelMapper.map(historyEntity, userEntity));
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(historyItemModels -> {
                    Collections.reverse(historyItemModels);

                    mHistoryModels.clear();
                    mHistoryModels.addAll(historyItemModels);

                    getView().updateItems();
                }, e -> {
                    LOGGER.error("Failed to find recent nearby items", e);
                    getView().showSnackBar(R.string.error_process);
                });

        reusableCompositeSubscription.add(subscription);
    }

    public void onCreateItemView(HistoryItemView historyItemView) {
        HistoryItemPresenter historyItemPresenter = new HistoryItemPresenter();
        historyItemPresenter.onCreateItemView(historyItemView);
        historyItemView.setItemPresenter(historyItemPresenter);
    }

    public final class HistoryItemPresenter extends BaseItemPresenter<HistoryItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(mHistoryModels.get(position));
        }

        public void onClick(HistoryModel model) {
            HistoryBundleData data = new HistoryBundleData();

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
                HistoryBundleData.Weather weather = new HistoryBundleData.Weather();
                weather.conditions = model.weather.conditions;
                weather.humidity = model.weather.humidity;
                weather.temperature = model.weather.temperature;
                data.weather = weather;
            }

            data.timestamp = model.passingTime;

            getView().showHistoryFragment(data);
        }

        public void onAdd(String otherUserId) {
            Subscription subscription = mSaveFavoriteUseCase
                    .execute(MyUser.getUid(), otherUserId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(entity -> {
                        getItemView().closeItem();
                        getView().showSnackBar(R.string.message_added);
                    }, e -> {
                        LOGGER.error("Failed to add favorite", e);
                        getView().showSnackBar(R.string.error_not_added);
                    });

            reusableCompositeSubscription.add(subscription);
        }
    }
}

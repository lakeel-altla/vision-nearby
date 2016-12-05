package com.lakeel.altla.vision.nearby.presentation.presenter.recently;

import android.support.annotation.IntRange;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.data.entity.UserEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindRecentlyUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.intent.RecentlyBundleData;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.RecentlyItemModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.RecentlyItemModel;
import com.lakeel.altla.vision.nearby.presentation.view.RecentlyItemView;
import com.lakeel.altla.vision.nearby.presentation.view.RecentlyListView;

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

public final class RecentlyListPresenter extends BasePresenter<RecentlyListView> {

    @Inject
    FindUserUseCase mFindUserUseCase;

    @Inject
    FindFavoriteUseCase mFindFavoriteUseCase;

    @Inject
    FindRecentlyUseCase mFindRecentlyUseCase;

    @Inject
    SaveFavoriteUseCase mSaveFavoriteUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(RecentlyListPresenter.class);

    private RecentlyItemModelMapper mRecentlyItemModelMapper = new RecentlyItemModelMapper();

    private final List<RecentlyItemModel> mRecentlyItemModels = new ArrayList<>();

    public List<RecentlyItemModel> getItems() {
        return mRecentlyItemModels;
    }

    @Inject
    RecentlyListPresenter() {
    }

    @Override
    public void onResume() {
        Subscription subscription = mFindRecentlyUseCase
                .execute(MyUser.getUid())
                .flatMap(entity -> {
                    Observable<UserEntity> itemsObservable = mFindUserUseCase.execute(entity.userId).subscribeOn(Schedulers.io()).toObservable();
                    return Observable.zip(Observable.just(entity), itemsObservable, (recentlyEntity, itemsEntity) ->
                            mRecentlyItemModelMapper.map(recentlyEntity, itemsEntity));
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recentlyItemModels -> {
                    Collections.reverse(recentlyItemModels);

                    mRecentlyItemModels.clear();
                    mRecentlyItemModels.addAll(recentlyItemModels);

                    getView().updateItems();
                }, e -> {
                    LOGGER.error("Failed to find recent nearby items", e);
                    getView().showSnackBar(R.string.error_process);
                });

        reusableCompositeSubscription.add(subscription);
    }

    public void onCreateItemView(RecentlyItemView recentlyItemView) {
        RecentlyItemPresenter recentlyItemPresenter = new RecentlyItemPresenter();
        recentlyItemPresenter.onCreateItemView(recentlyItemView);
        recentlyItemView.setItemPresenter(recentlyItemPresenter);
    }

    public final class RecentlyItemPresenter extends BaseItemPresenter<RecentlyItemView> {

        @Override
        public void onBind(@IntRange(from = 0) int position) {
            getItemView().showItem(mRecentlyItemModels.get(position));
        }

        public void onClick(RecentlyItemModel model) {
            RecentlyBundleData data = new RecentlyBundleData();
            data.userId = model.userId;
            data.userName = model.name;

            LocationModel locationModel = model.locationModel;
            if (locationModel != null) {
                data.latitude = locationModel.mLatitude;
                data.longitude = locationModel.mLongitude;
            }

            if (model.detectedActivity != null) {
                data.detectedActivity = model.detectedActivity;
            }

            if (model.weather != null) {
                RecentlyBundleData.Weather weather = new RecentlyBundleData.Weather();
                weather.conditions = model.weather.conditions;
                weather.humidity = model.weather.humidity;
                weather.temperature = model.weather.temperature;
                data.weather = weather;
            }

            data.timestamp = model.passingTime;

            getView().showRecentlyUserActivity(data);
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

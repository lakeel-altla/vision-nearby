package com.lakeel.altla.vision.nearby.presentation.presenter.recently;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindItemUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindRecentlyUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.presentation.intent.RecentlyIntentData;
import com.lakeel.altla.vision.nearby.presentation.presenter.BaseItemPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.RecentlyItemModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.LocationModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.RecentlyItemModel;
import com.lakeel.altla.vision.nearby.presentation.view.RecentlyItemView;
import com.lakeel.altla.vision.nearby.presentation.view.RecentlyView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class RecentlyPresenter extends BasePresenter<RecentlyView> {

    @Inject
    FindItemUseCase mFindItemUseCase;

    @Inject
    FindFavoriteUseCase mFindFavoriteUseCase;

    @Inject
    FindRecentlyUseCase mFindRecentlyUseCase;

    @Inject
    SaveFavoriteUseCase mSaveFavoriteUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(RecentlyPresenter.class);

    private RecentlyItemModelMapper mRecentlyItemModelMapper = new RecentlyItemModelMapper();

    private final List<RecentlyItemModel> mRecentlyItemModels = new ArrayList<>();

    public List<RecentlyItemModel> getItems() {
        return mRecentlyItemModels;
    }

    @Inject
    RecentlyPresenter() {
    }

    @Override
    public void onResume() {
        Subscription subscription = mFindRecentlyUseCase
                .execute()
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
        mCompositeSubscription.add(subscription);
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
            RecentlyIntentData data = new RecentlyIntentData();
            data.mId = model.mId;
            data.mKey = model.mKey;

            LocationModel locationModel = model.mLocationModel;
            if (locationModel != null) {
                data.mLatitude = locationModel.mLatitude;
                data.mLongitude = locationModel.mLongitude;

                LocationModel.LocationTextModel locationTextModel = locationModel.mLocationTextModel;
                if (locationTextModel != null) {
                    String language = Locale.getDefault().getLanguage();
                    String locationText = locationTextModel.mTextMap.get(language);
                    if (!StringUtils.isEmpty(locationText)) {
                        data.mLocationText = locationText;
                    }
                }
            }

            if (model.mUserActivity != null) {
                data.mUserActivity = model.mUserActivity;
            }

            if (model.mWeather != null) {
                RecentlyIntentData.Weather weather = new RecentlyIntentData.Weather();
                weather.mConditions = model.mWeather.mConditions;
                weather.mHumidity = model.mWeather.humidity;
                weather.mTemperature = model.mWeather.temparature;
                data.mWeather = weather;
            }

            data.mTimestamp = model.mPassingTime;

            getView().showRecentlyUserActivity(data);
        }

        public void onAdd(String id) {
            Subscription subscription = mSaveFavoriteUseCase
                    .execute(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(entity -> {
                        getItemView().closeItem();
                        getView().showSnackBar(R.string.message_added);
                    }, e -> {
                        LOGGER.error("Failed to add favorite", e);
                        getView().showSnackBar(R.string.error_not_added);
                    });
            mCompositeSubscription.add(subscription);
        }
    }
}

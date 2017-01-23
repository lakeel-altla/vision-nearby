package com.lakeel.altla.vision.nearby.presentation.presenter.favorite;

import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConnectionUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserBeaconIdsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.PresencesModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.UserModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteUserView;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class FavoriteUserPresenter extends BasePresenter<FavoriteUserView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindConnectionUseCase findConnectionUseCase;

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindLineLinkUseCase findLineLinkUseCase;

    @Inject
    FindUserBeaconIdsUseCase findUserBeaconIdsUseCase;

    private PresencesModelMapper presencesModelMapper = new PresencesModelMapper();

    private UserModelMapper userModelMapper = new UserModelMapper();

    private String favoriteUserId;

    private String favoriteUserName;

    @Inject
    FavoriteUserPresenter() {
    }

    public void setUserIdAndUserName(String favoriteUserId, String favoriteUserName) {
        this.favoriteUserId = favoriteUserId;
        this.favoriteUserName = favoriteUserName;
    }

    public void onActivityCreated() {
        analyticsReporter.viewFavoriteItem(favoriteUserId, favoriteUserName);

        // Presence.
        Subscription subscription = findConnectionUseCase.execute(favoriteUserId)
                .map(entity -> presencesModelMapper.map(entity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showPresence(model), new ErrorAction<>());
        subscriptions.add(subscription);

        // Profile.
        Subscription subscription1 = findUserUseCase.execute(favoriteUserId)
                .map(entity -> userModelMapper.map(entity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showProfile(model), new ErrorAction<>());
        subscriptions.add(subscription1);

        // LINE URL.
        Subscription subscription2 = findLineLinkUseCase.execute(favoriteUserId)
                .toObservable()
                .filter(entity -> entity != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> getView().showLineUrl(entity.url), new ErrorAction<>());
        subscriptions.add(subscription2);
    }

    public void onEstimateDistanceMenuClick() {
        Subscription subscription = findUserBeaconIdsUseCase.execute(favoriteUserId)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beacons -> {
                    analyticsReporter.estimateDistance(favoriteUserName);

                    ArrayList<String> beaconIds = new ArrayList<>(beacons.size());
                    beaconIds.addAll(beacons);
                    getView().showDistanceEstimationFragment(beaconIds, favoriteUserName);
                }, new ErrorAction<>());
        subscriptions.add(subscription);
    }
}

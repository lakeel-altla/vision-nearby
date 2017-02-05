package com.lakeel.altla.vision.nearby.presentation.presenter.favorite;

import com.lakeel.altla.vision.nearby.domain.usecase.FindAllPassingTimeUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindAllUserBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConnectionUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLatestNearbyHistoryUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.FavoriteUserModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteUserModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteUserView;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class FavoriteUserPresenter extends BasePresenter<FavoriteUserView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindLatestNearbyHistoryUseCase findLatestNearbyHistoryUseCase;

    @Inject
    FindConnectionUseCase findConnectionUseCase;

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindLineLinkUseCase findLineLinkUseCase;

    @Inject
    FindAllPassingTimeUseCase findAllPassingTimeUseCase;

    @Inject
    FindAllUserBeaconUseCase findAllUserBeaconUseCase;

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private final FavoriteUserModelMapper modelMapper = new FavoriteUserModelMapper();

    private FavoriteUserModel model;

    private String favoriteUserId;

    private String favoriteUserName;

    private boolean isMapReadied;

    @Inject
    FavoriteUserPresenter() {
    }

    public void setUserIdAndUserName(String favoriteUserId, String favoriteUserName) {
        this.favoriteUserId = favoriteUserId;
        this.favoriteUserName = favoriteUserName;
    }

    public void onActivityCreated() {
        analyticsReporter.viewFavoriteItem(favoriteUserId, favoriteUserName);

        Subscription subscription = findLatestNearbyHistoryUseCase.execute(favoriteUserId)
                .map(history -> {
                    model = modelMapper.map(history);
                    return model;
                })
                .flatMap(model1 -> findUser(model1.userId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    analyticsReporter.viewFavoriteItem(model.userId, model.userName);

                    getView().showProfile(model);
                    getView().showPassingData(model);

                    if (isMapReadied) {
                        onMapReady();
                    }

                    showPresence(model.userId);
                    showTimes(model.userId);
                    showLineUrl(model.userId);
                }, new ErrorAction<>());
        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unSubscribe();
    }

    public void onMapReady() {
        isMapReadied = true;
        if (model.latitude == null && model.longitude == null) {
            getView().hideLocation();
        } else {
            getView().showLocation(model.latitude, model.longitude);
        }
    }

    public void onEstimateDistanceMenuClick() {
        Subscription subscription = findAllUserBeaconUseCase.execute(favoriteUserId)
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

    private Single<FavoriteUserModel> findUser(String userId) {
        return findUserUseCase.execute(userId)
                .map(user -> {
                    model.userName = user.name;
                    model.imageUri = user.imageUri;
                    model.email = user.email;
                    return model;
                });
    }

    private void showPresence(String userId) {
        Subscription subscription = findConnectionUseCase.execute(userId)
                .map(connection -> {
                    model.isConnected = connection.isConnected;
                    model.lastOnlineTime = connection.lastOnlineTime;
                    return model;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showPresence(model), new ErrorAction<>());
        subscriptions.add(subscription);
    }

    private void showTimes(String userId) {
        Subscription timesSubscription = findAllPassingTimeUseCase.execute(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(times -> getView().showTimes(times), new ErrorAction<>());
        subscriptions.add(timesSubscription);
    }

    private void showLineUrl(String userId) {
        Subscription subscription = findLineLinkUseCase.execute(userId)
                .toObservable()
                .filter(entity -> entity != null)
                .map(entity -> entity.url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lineUrl -> getView().showLineUrl(lineUrl), new ErrorAction<>());
        subscriptions.add(subscription);
    }
}

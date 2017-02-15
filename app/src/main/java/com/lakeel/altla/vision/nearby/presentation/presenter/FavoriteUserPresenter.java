package com.lakeel.altla.vision.nearby.presentation.presenter;

import android.os.Bundle;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindAllPassingTimeUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindAllUserBeaconUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConnectionUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLatestNearbyHistoryUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.FavoriteUserModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteUserModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteUserView;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.EstimationTarget;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.FavoriteUser;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import javax.inject.Inject;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteUserPresenter.class);

    private static final String BUNDLE_FAVORITE_USER = "favoriteUser";

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private FavoriteUserModel model;

    private FavoriteUser favoriteUser;

    private boolean isMapReadied;

    @Inject
    FavoriteUserPresenter() {
    }

    public void onCreateView(FavoriteUserView view, Bundle bundle) {
        super.onCreateView(view);
        this.favoriteUser = (FavoriteUser) bundle.getSerializable(BUNDLE_FAVORITE_USER);
    }

    public void onActivityCreated() {
        getView().showTitle(favoriteUser.name);
    }

    public void onResume() {
        analyticsReporter.viewFavoriteItem(favoriteUser.userId, favoriteUser.name);

        Subscription subscription = findLatestNearbyHistoryUseCase.execute(favoriteUser.userId)
                .map(nearbyHistory -> {
                    model = FavoriteUserModelMapper.map(nearbyHistory);
                    return model;
                })
                .flatMap(model ->
                        findUserUseCase.execute(favoriteUser.userId)
                                .map(user -> {
                                    this.model.userName = user.name;
                                    this.model.imageUri = user.imageUri;
                                    this.model.email = user.email;
                                    return this.model;
                                }))
                .flatMap(model ->
                        findConnectionUseCase.execute(favoriteUser.userId)
                                .map(connection -> {
                                    this.model.isConnected = connection.isConnected;
                                    this.model.lastOnlineTime = (Long) connection.lastOnlineTime;
                                    return this.model;
                                }))
                .flatMap(model ->
                        findAllPassingTimeUseCase.execute(favoriteUser.userId)
                                .map(times -> {
                                    this.model.times = times;
                                    return this.model;
                                }))
                .flatMapObservable(model ->
                        findLineLinkUseCase.execute(favoriteUser.userId)
                                .toObservable()
                                .filter(lineLink -> lineLink != null)
                                .map(lineLink -> {
                                    if (!StringUtils.isEmpty(lineLink.url)) {
                                        this.model.lineUrl = lineLink.url;
                                    }
                                    return this.model;
                                }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (isMapReadied) {
                        onMapReady();
                    }

                    getView().updateModel(model);
                }, e -> {
                    LOGGER.error("Failed.", e);
                    getView().showSnackBar(R.string.snackBar_error_failed);
                });
        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unSubscribe();
    }

    public void onMapReady() {
        isMapReadied = true;
        if (model == null || model.locationModel == null) {
            getView().hideLocation();
        } else {
            getView().showLocation(model.locationModel.latitude, model.locationModel.longitude);
        }
    }

    public void onEstimateDistanceMenuClick() {
        Subscription subscription = findAllUserBeaconUseCase.execute(favoriteUser.userId)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beacons -> {
                    analyticsReporter.estimateDistance(favoriteUser.name);

                    ArrayList<String> beaconIds = new ArrayList<>(beacons.size());
                    beaconIds.addAll(beacons);
                    EstimationTarget estimationTarget = new EstimationTarget(favoriteUser.name, beaconIds);
                    getView().showDistanceEstimationFragment(estimationTarget);
                }, e -> {
                    LOGGER.error("Failed.", e);
                    getView().showSnackBar(R.string.snackBar_error_failed);
                });
        subscriptions.add(subscription);
    }
}

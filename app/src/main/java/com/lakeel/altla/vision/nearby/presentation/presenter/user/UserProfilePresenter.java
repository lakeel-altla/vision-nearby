package com.lakeel.altla.vision.nearby.presentation.presenter.user;

import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPresenceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserBeaconsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.PresencesModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.UserModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.UserProfileView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class UserProfilePresenter extends BasePresenter<UserProfileView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindPresenceUseCase findPresenceUseCase;

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindLineLinkUseCase findLineLinkUseCase;

    @Inject
    FindUserBeaconsUseCase findUserBeaconsUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfilePresenter.class);

    private PresencesModelMapper presencesModelMapper = new PresencesModelMapper();

    private UserModelMapper userModelMapper = new UserModelMapper();

    private String userId;

    private String userName;

    @Inject
    UserProfilePresenter() {
    }

    public void setUserData(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public void onActivityCreated() {
        analyticsReporter.viewFavoriteItem(userId, userName);

        Subscription presenceSubscription = findPresenceUseCase
                .execute(userId)
                .map(entity -> presencesModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showPresence(model),
                        e -> LOGGER.error("Failed to findList presence.", e));
        subscriptions.add(presenceSubscription);

        Subscription userSubscription = findUserUseCase
                .execute(userId)
                .map(entity -> userModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showProfile(model),
                        e -> LOGGER.error("Failed to findList user.", e));
        subscriptions.add(userSubscription);

        Subscription lineLinkSubscription = findLineLinkUseCase
                .execute(userId)
                .map(entity -> {
                    if (entity == null) return StringUtils.EMPTY;
                    return entity.url;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lineUrl -> getView().showLineUrl(lineUrl),
                        e -> LOGGER.error("Failed to findList LINE links.", e)
                );
        subscriptions.add(lineLinkSubscription);
    }

    public void onFindDeviceMenuClick() {
        Subscription subscription = findUserBeaconsUseCase
                .execute(userId)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beacons -> {
                    analyticsReporter.estimateDistance(userName);

                    ArrayList<String> beaconIds = new ArrayList<>(beacons.size());
                    beaconIds.addAll(beacons);
                    getView().showDistanceEstimationFragment(beaconIds, userName);
                }, e -> {
                    LOGGER.error("Failed to findList user beacons.", e);
                });
        subscriptions.add(subscription);
    }
}

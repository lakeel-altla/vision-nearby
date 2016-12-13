package com.lakeel.altla.vision.nearby.presentation.presenter.user;

import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindCmJidUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConfigsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPresenceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserBeaconsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveCmFavoritesUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.data.CmFavoriteData;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.CmFavoritesDataMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.PresencesModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.UserModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.UserProfileView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class UserProfilePresenter extends BasePresenter<UserProfileView> {

    @Inject
    FindPresenceUseCase findPresenceUseCase;

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindConfigsUseCase findConfigsUseCase;

    @Inject
    FindLineLinkUseCase findLineLinkUseCase;

    @Inject
    FindUserBeaconsUseCase findUserBeaconsUseCase;

    @Inject
    FindCmJidUseCase findCmJidUseCase;

    @Inject
    SaveCmFavoritesUseCase saveCmFavoritesUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfilePresenter.class);

    private PresencesModelMapper presencesModelMapper = new PresencesModelMapper();

    private UserModelMapper userModelMapper = new UserModelMapper();

    private CmFavoritesDataMapper cmFavoritesDataMapper = new CmFavoritesDataMapper();

    private String userId;

    private String userName;

    private boolean isCmLinkClicked;

    @Inject
    UserProfilePresenter() {
    }

    public void setUserData(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public void onActivityCreated() {
        Subscription presenceSubscription = findPresenceUseCase
                .execute(userId)
                .map(entity -> presencesModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showPresence(model),
                        e -> LOGGER.error("Failed to find presence.", e));
        subscriptions.add(presenceSubscription);

        Subscription userSubscription = findUserUseCase
                .execute(userId)
                .map(entity -> userModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showProfile(model),
                        e -> LOGGER.error("Failed to find user.", e));
        subscriptions.add(userSubscription);

        Subscription configsSubscription = findConfigsUseCase
                .execute()
                .map(entity -> entity.isCmLinkEnabled)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isCmLinkEnabled -> {
                    isCmLinkClicked = isCmLinkEnabled;
                    getView().initializeOptionMenu();
                }, e -> LOGGER.error("Failed to find configs.", e));
        subscriptions.add(configsSubscription);

        Subscription lineLinkSubscription = findLineLinkUseCase
                .execute(userId)
                .map(entity -> {
                    if (entity == null) return StringUtils.EMPTY;
                    return entity.url;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lineUrl -> getView().showLineUrl(lineUrl),
                        e -> LOGGER.error("Failed to find LINE links.", e)
                );
        subscriptions.add(lineLinkSubscription);
    }

    public boolean isCmLinkEnabled() {
        return isCmLinkClicked;
    }

    public void onShare() {
        getView().showShareSheet();
    }

    public void onFindDeviceMenuClick() {
        Subscription subscription = findUserBeaconsUseCase
                .execute(userId)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beacons -> {
                    ArrayList<String> beaconIds = new ArrayList<>(beacons.size());
                    beaconIds.addAll(beacons);
                    getView().showFindNearbyDeviceFragment(beaconIds, userName);
                }, e -> {
                    LOGGER.error("Failed to find user beacons.", e);
                });
        subscriptions.add(subscription);
    }

    public void onCmMenuClick() {
        Subscription subscription = findCmJidUseCase
                .execute(userId)
                .map(jid -> cmFavoritesDataMapper.map(jid))
                .flatMap(this::saveCmFavorites)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> getView().showSnackBar(R.string.message_added),
                        e -> {
                            LOGGER.error("Failed to add to CM favorites.", e);
                            getView().showSnackBar(R.string.error_not_added);
                        });
        subscriptions.add(subscription);
    }

    Single<Timestamp> saveCmFavorites(CmFavoriteData data) {
        return saveCmFavoritesUseCase.execute(data).subscribeOn(Schedulers.io());
    }
}

package com.lakeel.altla.vision.nearby.presentation.presenter.favorites;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindCMJidUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.CmFavoritesDataMapper;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConfigsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineUrlUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPresenceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserBeaconsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveCMFavoritesUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.PresencesModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.UserModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.FavoriteView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class FavoritePresenter extends BasePresenter<FavoriteView> {

    @Inject
    FindPresenceUseCase findPresenceUseCase;

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindConfigsUseCase findConfigsUseCase;

    @Inject
    FindLineUrlUseCase findLineUrlUseCase;

    @Inject
    FindUserBeaconsUseCase findUserBeaconsUseCase;

    @Inject
    FindCMJidUseCase findCMJidUseCase;

    @Inject
    SaveCMFavoritesUseCase saveCMFavoritesUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(FavoritePresenter.class);

    private PresencesModelMapper presencesModelMapper = new PresencesModelMapper();

    private UserModelMapper userModelMapper = new UserModelMapper();

    private CmFavoritesDataMapper cmFavoritesDataMapper = new CmFavoritesDataMapper();

    private String userId;

    private String userName;

    private boolean isCmLinkClicked;

    @Inject
    FavoritePresenter() {
    }

    @Override
    public void onActivityCreated() {
        Subscription subscription = findPresenceUseCase
                .execute(userId)
                .map(entity -> presencesModelMapper.map(entity))
                .doOnSuccess(model -> getView().showPresence(model))
                .flatMap(model -> findUserUseCase.execute(userId).subscribeOn(Schedulers.io()))
                .map(entity -> userModelMapper.map(entity))
                .doOnSuccess(model -> getView().showProfile(model))
                .flatMap(model -> findConfigsUseCase.execute().subscribeOn(Schedulers.io()))
                .map(entity -> entity.isCmLinkEnabled)
                .doOnSuccess(isCmLinkEnabled -> {
                    isCmLinkClicked = isCmLinkEnabled;
                    getView().initializeOptionMenu();
                })
                .flatMap(aBoolean -> findLineUrlUseCase.execute(userId).subscribeOn(Schedulers.io()))
                .map(entity -> {
                    if (entity == null) return StringUtils.EMPTY;
                    return entity.url;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lineUrl -> getView().showLineUrl(lineUrl),
                        e -> LOGGER.error("Failed to find user data.", e)
                );

        reusableCompositeSubscription.add(subscription);
    }

    public void setUserData(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public boolean isCmLinkEnabled() {
        return isCmLinkClicked;
    }

    public void onShare() {
        getView().showShareSheet();
    }

    public void onNearbyDeviceMenuClicked() {
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
        reusableCompositeSubscription.add(subscription);
    }

    public void onCmMenuClicked() {
        Subscription subscription = findCMJidUseCase
                .execute(userId)
                .map(jid -> cmFavoritesDataMapper.map(jid))
                .flatMap(data -> saveCMFavoritesUseCase.execute(data).subscribeOn(Schedulers.io()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> getView().showSnackBar(R.string.message_added),
                        e -> {
                            LOGGER.error("Failed to add to CM favorites.", e);
                            getView().showSnackBar(R.string.error_not_added);
                        });
        reusableCompositeSubscription.add(subscription);
    }
}

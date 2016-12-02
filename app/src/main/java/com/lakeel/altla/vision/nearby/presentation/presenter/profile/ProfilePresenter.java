package com.lakeel.altla.vision.nearby.presentation.presenter.profile;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConfigsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLINEUrlUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPresenceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserBeaconsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserToCmFavoritesUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.ItemModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.PresencesModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.ProfileView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class ProfilePresenter extends BasePresenter<ProfileView> {

    @Inject
    FindPresenceUseCase findPresenceUseCase;

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindConfigsUseCase findConfigsUseCase;

    @Inject
    FindLINEUrlUseCase findLINEUrlUseCase;

    @Inject
    FindUserBeaconsUseCase findUserBeaconsUseCase;

    @Inject
    SaveUserToCmFavoritesUseCase saveUserToCmFavoritesUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfilePresenter.class);

    private PresencesModelMapper presencesModelMapper = new PresencesModelMapper();

    private ItemModelMapper itemModelMapper = new ItemModelMapper();

    private String userId;

    private String userName;

    private boolean isCmLinkClicked;

    @Inject
    ProfilePresenter() {
    }

    @Override
    public void onActivityCreated() {
        Subscription subscription = findPresenceUseCase
                .execute(userId)
                .map(entity -> presencesModelMapper.map(entity))
                .doOnSuccess(model -> getView().showPresence(model))
                .flatMap(model -> findUserUseCase.execute(userId).subscribeOn(Schedulers.io()))
                .map(entity -> itemModelMapper.map(entity))
                .doOnSuccess(model -> getView().showProfile(model))
                .flatMap(model -> findConfigsUseCase.execute().subscribeOn(Schedulers.io()))
                .map(entity -> entity.isCmLinkEnabled)
                .doOnSuccess(isCmLinkEnabled -> {
                    isCmLinkClicked = isCmLinkEnabled;
                    getView().initializeOptionMenu();
                })
                .flatMap(aBoolean -> findLINEUrlUseCase.execute(userId).subscribeOn(Schedulers.io()))
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

    public void onFindNearbyDeviceMenuClicked() {
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
        Subscription subscription = saveUserToCmFavoritesUseCase
                .execute(userId)
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

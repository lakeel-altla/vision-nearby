package com.lakeel.altla.vision.nearby.presentation.presenter.activity;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.data.entity.LINELinksEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConfigsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindItemUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLINEUrlUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPresenceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserBeaconsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserToCmFavoritesUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.BeaconModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.ItemModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.PresencesModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.BeaconModel;
import com.lakeel.altla.vision.nearby.presentation.view.FavoritesUserActivityView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class FavoritesUserActivityPresenter extends BasePresenter<FavoritesUserActivityView> {

    @Inject
    FindPresenceUseCase mFindPresenceUseCase;

    @Inject
    FindItemUseCase mFindItemUseCase;

    @Inject
    SaveUserToCmFavoritesUseCase mSaveUserToCmFavoritesUseCase;

    @Inject
    FindConfigsUseCase mFindConfigsUseCase;

    @Inject
    FindLINEUrlUseCase mFindLINEUrlUseCase;

    @Inject
    FindUserBeaconsUseCase mFindUserBeaconsUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(FavoritesUserActivityPresenter.class);

    private PresencesModelMapper mPresencesModelMapper = new PresencesModelMapper();

    private ItemModelMapper mItemModelMapper = new ItemModelMapper();

    private BeaconModelMapper mBeaconModelMapper = new BeaconModelMapper();

    private String mUserId;

    private String mUserName;

    private boolean mCmLinkEnabled;

    @Inject
    FavoritesUserActivityPresenter() {
    }

    @Override
    public void onResume() {

        // Show presence.
        Subscription subscription1 = mFindPresenceUseCase
                .execute(mUserId)
                .map(entity -> mPresencesModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showPresence(model),
                        e -> LOGGER.error("Failed to find presence.", e));
        mCompositeSubscription.add(subscription1);

        // Show profile.
        Subscription subscription2 = mFindItemUseCase.
                execute(mUserId)
                .subscribeOn(Schedulers.io())
                .map(entity -> mItemModelMapper.map(entity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    getView().showTitle(model.mName);
                    getView().showProfile(model);
                }, e -> LOGGER.error("Failed to find item.", e));
        mCompositeSubscription.add(subscription2);

        Subscription subscription3 = mFindConfigsUseCase
                .execute()
                .map(entity -> entity.isCmLinkEnabled)
                .flatMap(new Func1<Boolean, Single<LINELinksEntity>>() {
                    @Override
                    public Single<LINELinksEntity> call(Boolean bool) {
                        mCmLinkEnabled = bool;
                        return mFindLINEUrlUseCase
                                .execute(mUserId)
                                .subscribeOn(Schedulers.io());
                    }
                })
                .map(entity -> entity.url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lineUrl -> {
                    getView().showLineUrl(lineUrl);
                    getView().initializeOptionMenu();
                }, e -> LOGGER.error("Failed to find config settings.", e));
        mCompositeSubscription.add(subscription3);
    }

    public void setUserData(String userId, String userName) {
        mUserId = userId;
        mUserName = userName;
    }

    public void onShare() {
        getView().showShareSheet();
    }

    public void onFindNearbyDeviceMenuClicked() {
        Subscription subscription = mFindUserBeaconsUseCase
                .execute(mUserId)
                .map(entity -> mBeaconModelMapper.map(entity))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(models -> {
                    ArrayList<String> beaconIds = new ArrayList<>(models.size());
                    for (BeaconModel model : models) {
                        beaconIds.add(model.mBeaconId);
                    }
                    getView().showFindNearbyDeviceFragment(beaconIds, mUserName);
                }, e -> {
                    LOGGER.error("Failed to find user beacons.", e);
                });
        mCompositeSubscription.add(subscription);
    }

    public void onAddToCmFavorites() {
        Subscription subscription = mSaveUserToCmFavoritesUseCase
                .execute(mUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> getView().showSnackBar(R.string.message_added),
                        e -> {
                            LOGGER.error("Failed to add to CM favorites.", e);
                            getView().showSnackBar(R.string.error_not_added);
                        });
        mCompositeSubscription.add(subscription);
    }

    public boolean isCmLinkEnabled() {
        return mCmLinkEnabled;
    }
}

package com.lakeel.altla.vision.nearby.presentation.presenter.activity;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.data.entity.LINELinksEntity;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConfigsUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindItemUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLINEUrlUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLocationTextUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPresenceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindTimesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveLocationTextUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserToCmFavoritesUseCase;
import com.lakeel.altla.vision.nearby.presentation.intent.RecentlyIntentData;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.ItemModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.LocationTextMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.PresencesModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.RecentlyUserActivityView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class RecentlyUserActivityPresenter extends BasePresenter<RecentlyUserActivityView> {

    @Inject
    FindItemUseCase mFindItemUseCase;

    @Inject
    FindTimesUseCase mFindTimesUseCase;

    @Inject
    SaveFavoriteUseCase mSaveFavoriteUseCase;

    @Inject
    FindPresenceUseCase mFindPresenceUseCase;

    @Inject
    FindLocationTextUseCase mFindLocationTextUseCase;

    @Inject
    FindFavoriteUseCase mFindFavoriteUseCase;

    @Inject
    SaveLocationTextUseCase mSaveLocationTextUseCase;

    @Inject
    SaveUserToCmFavoritesUseCase mSaveUserToCmFavoritesUseCase;

    @Inject
    FindConfigsUseCase mFindConfigsUseCase;

    @Inject
    FindLINEUrlUseCase mFindLINEUrlUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(RecentlyUserActivityPresenter.class);

    private PresencesModelMapper mPresencesModelMapper = new PresencesModelMapper();

    private ItemModelMapper mItemModelMapper = new ItemModelMapper();

    private LocationTextMapper mLocationTextMapper = new LocationTextMapper();

    private String mRecentlyKey;

    private String mItemId;

    private String mLatitude;

    private String mLongitude;

    private String mLocationText;

    private boolean mCmLinkEnabled;

    @Inject
    RecentlyUserActivityPresenter() {
    }

    public void setData(RecentlyIntentData data) {
        mRecentlyKey = data.mKey;
        mItemId = data.mId;
        mLatitude = data.mLatitude;
        mLongitude = data.mLongitude;
        mLocationText = data.mLocationText;
    }

    @Override
    public void onResume() {
        // Show presence.
        Subscription subscription1 = mFindPresenceUseCase
                .execute(mItemId)
                .map(entity -> mPresencesModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showPresence(model),
                        e -> LOGGER.error("Failed to find presence.", e));
        mCompositeSubscription.add(subscription1);

        // Show number of times of passing.
        Subscription subscription2 = mFindTimesUseCase
                .execute(mItemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(times -> getView().showTimes(times),
                        e -> LOGGER.error("Failed to find times", e));
        mCompositeSubscription.add(subscription2);

        // Show profile.
        Subscription subscription3 = mFindItemUseCase.
                execute(mItemId)
                .subscribeOn(Schedulers.io())
                .map(entity -> mItemModelMapper.map(entity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    getView().showTitle(model.mName);
                    getView().showProfile(model);
                }, e -> LOGGER.error("Failed to find item.", e));
        mCompositeSubscription.add(subscription3);

        // Show location text.
        if (StringUtils.isEmpty(mLocationText)) {
            String language = Locale.getDefault().getLanguage();

            Subscription subscription4 = mFindLocationTextUseCase
                    .execute(language, mLatitude, mLongitude)
                    .map(entity -> mLocationTextMapper.map(entity))
                    .flatMap(new Func1<String, Single<String>>() {
                        @Override
                        public Single<String> call(String locationText) {
                            // Save location text in cache.
                            return mSaveLocationTextUseCase
                                    .execute(mRecentlyKey, language, locationText)
                                    .subscribeOn(Schedulers.io());
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(locationText -> {
                                if (!StringUtils.isEmpty(locationText)) {
                                    getView().showLocationText(locationText);
                                }
                            },
                            e -> LOGGER.error("Failed fetch location.", e));
            mCompositeSubscription.add(subscription4);
        } else {
            getView().showLocationText(mLocationText);
        }

        // Check if already have been added to the favorite.
        Subscription subscription5 = mFindFavoriteUseCase
                .execute(mItemId)
                .toObservable()
                .filter(entity -> entity == null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                    getView().showAddButton();
                }, e -> {
                    LOGGER.error("Failed to find favorite user", e);
                });
        mCompositeSubscription.add(subscription5);

        Subscription subscription6 = mFindConfigsUseCase
                .execute()
                .map(entity -> entity.isCmLinkEnabled)
                .flatMap(new Func1<Boolean, Single<LINELinksEntity>>() {
                    @Override
                    public Single<LINELinksEntity> call(Boolean bool) {
                        mCmLinkEnabled = bool;
                        return mFindLINEUrlUseCase.execute(mItemId).subscribeOn(Schedulers.io());
                    }
                })
                .map(lineLinksEntity -> lineLinksEntity.url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lineUrl -> {
                    getView().showLineUrl(lineUrl);
                    getView().initializeOptionMenu();
                }, e -> LOGGER.error("Failed to find config settings.", e));
        mCompositeSubscription.add(subscription6);
    }

    public boolean isCmLinkEnabled() {
        return mCmLinkEnabled;
    }

    public void onMapReady() {
        if (mLatitude == null && mLongitude == null) {
            getView().hideLocation();
        } else {
            getView().showLocationMap(mLatitude, mLongitude);
        }
    }

    public void onAdd() {
        Subscription subscription = mSaveFavoriteUseCase
                .execute(mItemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                            getView().hideAddButton();
                            getView().showSnackBar(R.string.message_added);
                        },
                        e -> {
                            LOGGER.error("Failed to add favorites.", e);
                            getView().showSnackBar(R.string.error_not_added);
                        });
        mCompositeSubscription.add(subscription);
    }

    public void onShare() {
        getView().showShareSheet();
    }

    public void onAddToCmFavorites() {
        Subscription subscription = mSaveUserToCmFavoritesUseCase
                .execute(mItemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> getView().showSnackBar(R.string.message_added),
                        e -> {
                            LOGGER.error("Failed to add to CM favorites.", e);
                            getView().showSnackBar(R.string.error_not_added);
                        });
        mCompositeSubscription.add(subscription);
    }
}

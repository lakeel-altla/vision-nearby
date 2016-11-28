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
    FindItemUseCase findItemUseCase;

    @Inject
    FindTimesUseCase findTimesUseCase;

    @Inject
    SaveFavoriteUseCase saveFavoriteUseCase;

    @Inject
    FindPresenceUseCase findPresenceUseCase;

    @Inject
    FindLocationTextUseCase findLocationTextUseCase;

    @Inject
    FindFavoriteUseCase findFavoriteUseCase;

    @Inject
    SaveLocationTextUseCase saveLocationTextUseCase;

    @Inject
    SaveUserToCmFavoritesUseCase saveUserToCmFavoritesUseCase;

    @Inject
    FindConfigsUseCase findConfigsUseCase;

    @Inject
    FindLINEUrlUseCase findLINEUrlUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(RecentlyUserActivityPresenter.class);

    private PresencesModelMapper presencesModelMapper = new PresencesModelMapper();

    private ItemModelMapper itemModelMapper = new ItemModelMapper();

    private LocationTextMapper locationTextMapper = new LocationTextMapper();

    private String recentlyKey;

    private String itemId;

    private String latitude;

    private String longitude;

    private String locationText;

    private boolean isCmLinkEnabled;

    @Inject
    RecentlyUserActivityPresenter() {
    }

    public void setData(RecentlyIntentData data) {
        recentlyKey = data.key;
        itemId = data.id;
        latitude = data.latitude;
        longitude = data.longitude;
        locationText = data.locationText;
    }

    @Override
    public void onResume() {
        // Show presence.
        Subscription subscription1 = findPresenceUseCase
                .execute(itemId)
                .map(entity -> presencesModelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showPresence(model),
                        e -> LOGGER.error("Failed to find presence.", e));
        mCompositeSubscription.add(subscription1);

        // Show number of times of passing.
        Subscription subscription2 = findTimesUseCase
                .execute(itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(times -> getView().showTimes(times),
                        e -> LOGGER.error("Failed to find times", e));
        mCompositeSubscription.add(subscription2);

        // Show profile.
        Subscription subscription3 = findItemUseCase.
                execute(itemId)
                .subscribeOn(Schedulers.io())
                .map(entity -> itemModelMapper.map(entity))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    getView().showTitle(model.mName);
                    getView().showProfile(model);
                }, e -> LOGGER.error("Failed to find item.", e));
        mCompositeSubscription.add(subscription3);

        // Show location text.
        if (StringUtils.isEmpty(locationText)) {
            String language = Locale.getDefault().getLanguage();

            Subscription subscription4 = findLocationTextUseCase
                    .execute(language, latitude, longitude)
                    .map(entity -> locationTextMapper.map(entity))
                    .flatMap(new Func1<String, Single<String>>() {
                        @Override
                        public Single<String> call(String locationText) {
                            // Save location text in cache.
                            return saveLocationTextUseCase
                                    .execute(recentlyKey, language, locationText)
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
            getView().showLocationText(locationText);
        }

        // Check if already have been added to the favorite.
        Subscription subscription5 = findFavoriteUseCase
                .execute(itemId)
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

        Subscription subscription6 = findConfigsUseCase
                .execute()
                .map(entity -> entity.isCmLinkEnabled)
                .flatMap(new Func1<Boolean, Single<LINELinksEntity>>() {
                    @Override
                    public Single<LINELinksEntity> call(Boolean bool) {
                        isCmLinkEnabled = bool;
                        return findLINEUrlUseCase
                                .execute(itemId)
                                .subscribeOn(Schedulers.io());
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
        return isCmLinkEnabled;
    }

    public void onMapReady() {
        if (latitude == null && longitude == null) {
            getView().hideLocation();
        } else {
            getView().showLocationMap(latitude, longitude);
        }
    }

    public void onAdd() {
        Subscription subscription = saveFavoriteUseCase
                .execute(itemId)
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
        Subscription subscription = saveUserToCmFavoritesUseCase
                .execute(itemId)
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

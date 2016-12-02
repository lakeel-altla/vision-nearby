package com.lakeel.altla.vision.nearby.presentation.presenter.recently;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLINEUrlUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLocationTextUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPresenceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindTimesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveLocationTextUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.ItemModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.PresencesModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.RecentlyView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class RecentlyPresenter extends BasePresenter<RecentlyView> {

    @Inject
    FindUserUseCase findUserUseCase;

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
    FindLINEUrlUseCase findLINEUrlUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(RecentlyPresenter.class);

    private PresencesModelMapper presencesModelMapper = new PresencesModelMapper();

    private ItemModelMapper itemModelMapper = new ItemModelMapper();

    private String userId;

    private String latitude;

    private String longitude;

    @Inject
    RecentlyPresenter() {
    }

    @Override
    public void onActivityCreated() {
        Subscription subscription = findPresenceUseCase
                .execute(userId)
                .map(entity -> presencesModelMapper.map(entity))
                .doOnSuccess(model -> getView().showPresence(model))
                .flatMap(presenceModel -> findTimesUseCase.execute(userId).subscribeOn(Schedulers.io()))
                .doOnSuccess(times -> getView().showTimes(times))
                .flatMap(times -> findUserUseCase.execute(userId).subscribeOn(Schedulers.io()))
                .map(entity -> itemModelMapper.map(entity))
                .doOnSuccess(model -> getView().showProfile(model))
                .flatMap(model -> findLINEUrlUseCase.execute(userId).subscribeOn(Schedulers.io()))
                .map(lineLinksEntity -> lineLinksEntity.url)
                .doOnSuccess(lineUrl -> getView().showLineUrl(lineUrl))
                .flatMapObservable(s -> findFavoriteUseCase.execute(userId).subscribeOn(Schedulers.io()).toObservable())
                .filter(entity -> entity == null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                    getView().showAddButton();
                }, e -> {
                    LOGGER.error("Failed to find the user data.", e);
                });

        reusableCompositeSubscription.add(subscription);
    }

    public void setUserLocationData(String userId, String latitude, String longitude) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
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
                .execute(userId)
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
        reusableCompositeSubscription.add(subscription);
    }
}

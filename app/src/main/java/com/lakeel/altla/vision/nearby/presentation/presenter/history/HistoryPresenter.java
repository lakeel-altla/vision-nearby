package com.lakeel.altla.vision.nearby.presentation.presenter.history;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineUrlUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLocationTextUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindPresenceUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindTimesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveLocationTextUseCase;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.PresencesModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.UserModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.HistoryView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class HistoryPresenter extends BasePresenter<HistoryView> {

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
    FindLineUrlUseCase findLineUrlUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryPresenter.class);

    private PresencesModelMapper presencesModelMapper = new PresencesModelMapper();

    private UserModelMapper userModelMapper = new UserModelMapper();

    private String otherUserId;

    private String latitude;

    private String longitude;

    @Inject
    HistoryPresenter() {
    }

    @Override
    public void onActivityCreated() {
        Subscription subscription = findPresenceUseCase
                .execute(otherUserId)
                .map(entity -> presencesModelMapper.map(entity))
                .doOnSuccess(model -> getView().showPresence(model))
                .flatMap(presenceModel -> findTimesUseCase.execute(MyUser.getUid(), otherUserId).subscribeOn(Schedulers.io()))
                .doOnSuccess(times -> getView().showTimes(times))
                .flatMap(times -> findUserUseCase.execute(otherUserId).subscribeOn(Schedulers.io()))
                .map(entity -> userModelMapper.map(entity))
                .doOnSuccess(model -> getView().showProfile(model))
                .flatMap(model -> findLineUrlUseCase.execute(otherUserId).subscribeOn(Schedulers.io()))
                .map(lineLinksEntity -> lineLinksEntity.url)
                .doOnSuccess(lineUrl -> getView().showLineUrl(lineUrl))
                .flatMapObservable(s -> findFavoriteUseCase.execute(MyUser.getUid(), otherUserId).subscribeOn(Schedulers.io()).toObservable())
                .filter(entity -> entity == null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> {
                    getView().showAddButton();
                }, e -> {
                    LOGGER.error("Failed to find the user data.", e);
                });

        reusableCompositeSubscription.add(subscription);
    }

    public void setUserLocationData(String userId, String latitude, String longitude) {
        this.otherUserId = userId;
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
                .execute(MyUser.getUid(), otherUserId)
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

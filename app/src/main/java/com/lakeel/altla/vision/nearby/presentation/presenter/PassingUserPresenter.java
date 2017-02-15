package com.lakeel.altla.vision.nearby.presentation.presenter;

import android.os.Bundle;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindAllPassingTimeUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConnectionUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindNearbyHistoryUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.UserPassingModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PassingUserModel;
import com.lakeel.altla.vision.nearby.presentation.view.PassingUserView;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class PassingUserPresenter extends BasePresenter<PassingUserView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindNearbyHistoryUseCase findNearbyHistoryUseCase;

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindAllPassingTimeUseCase findAllPassingTimeUseCase;

    @Inject
    SaveFavoriteUseCase saveFavoriteUseCase;

    @Inject
    FindConnectionUseCase findConnectionUseCase;

    @Inject
    FindFavoriteUseCase findFavoriteUseCase;

    @Inject
    FindLineLinkUseCase findLineLinkUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(PassingUserPresenter.class);

    private static final String BUNDLE_HISTORY_ID = "historyId";

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private PassingUserModel model;

    private String historyId;

    private boolean isMapReadied;

    private boolean isAdding = false;

    @Inject
    PassingUserPresenter() {
    }

    public void onCreateView(PassingUserView view, Bundle bundle) {
        super.onCreateView(view);
        historyId = bundle.getString(BUNDLE_HISTORY_ID);
    }

    public void onResume() {
        Subscription subscription = findNearbyHistoryUseCase.execute(historyId)
                .map(history -> {
                    model = UserPassingModelMapper.map(history);
                    return model;
                })
                .flatMap(model ->
                        findUserUseCase.execute(model.userId)
                                .map(user -> {
                                    this.model.userName = user.name;
                                    this.model.imageUri = user.imageUri;
                                    this.model.email = user.email;
                                    return this.model;
                                }))
                .flatMap(model ->
                        findConnectionUseCase.execute(model.userId)
                                .map(connection -> {
                                    this.model.isConnected = connection.isConnected;
                                    this.model.lastOnlineTime = (Long) connection.lastOnlineTime;
                                    return this.model;
                                }))
                .flatMap(model ->
                        findAllPassingTimeUseCase.execute(model.userId)
                                .map(times -> {
                                    this.model.times = times;
                                    return this.model;
                                }))
                .flatMap(model ->
                        findLineLinkUseCase.execute(model.userId)
                                .map(lineLink -> {
                                    if (lineLink != null) {
                                        this.model.lineUrl = lineLink.url;
                                    }
                                    return this.model;
                                }))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    showAddFavoriteButtonIfNeeded(model.userId);

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

    public void onAdd() {
        if (isAdding) {
            return;
        }
        isAdding = true;

        analyticsReporter.addFavorite(model.userId, model.userName);

        Subscription subscription = saveFavoriteUseCase.execute(model.userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> isAdding = false,
                        () -> {
                            isAdding = false;
                            getView().hideAddButton();
                            getView().showSnackBar(R.string.snackBar_message_added);
                        });
        subscriptions.add(subscription);
    }

    private void showAddFavoriteButtonIfNeeded(String favoriteUserId) {
        Subscription subscription = findFavoriteUseCase.execute(favoriteUserId)
                .toObservable()
                .filter(favorite -> favorite == null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favorite -> getView().showAddButton(),
                        e -> {
                            LOGGER.error("Failed.", e);
                            getView().showSnackBar(R.string.snackBar_error_failed);
                        });
        subscriptions.add(subscription);
    }
}
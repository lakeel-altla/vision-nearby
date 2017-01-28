package com.lakeel.altla.vision.nearby.presentation.presenter.user;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConnectionUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindHistoryUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindTimesUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.UserPassingModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserPassingModel;
import com.lakeel.altla.vision.nearby.presentation.view.PassingUserView;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;

import javax.inject.Inject;

import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class PassingUserPresenter extends BasePresenter<PassingUserView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    FindHistoryUseCase findHistoryUseCase;

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    FindTimesUseCase findTimesUseCase;

    @Inject
    SaveFavoriteUseCase saveFavoriteUseCase;

    @Inject
    FindConnectionUseCase findConnectionUseCase;

    @Inject
    FindFavoriteUseCase findFavoriteUseCase;

    @Inject
    FindLineLinkUseCase findLineLinkUseCase;

    private UserPassingModel model = new UserPassingModel();

    private UserPassingModelMapper modelMapper = new UserPassingModelMapper();

    private String historyId;

    private boolean isMapReadied;

    @Inject
    PassingUserPresenter() {
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public void onActivityCreated() {
        Subscription subscription = findHistoryUseCase.execute(historyId)
                .map(history -> {
                    model = modelMapper.map(history);
                    return model;
                })
                .flatMap(model1 -> findUser(model1.userId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    analyticsReporter.viewHistoryItem(model.userId, model.userName);

                    getView().showProfile(model);
                    getView().showPassingData(model);

                    if (isMapReadied) {
                        onMapReady();
                    }

                    showPresence(model.userId);
                    showTimes(model.userId);
                    showLineUrl(model.userId);
                    showAddFavoriteButtonIfNeeded(model.userId);
                }, new ErrorAction<>());
        subscriptions.add(subscription);
    }

    public void onMapReady() {
        isMapReadied = true;
        if (model.latitude == null && model.longitude == null) {
            getView().hideLocation();
        } else {
            getView().showLocationMap(model.latitude, model.longitude);
        }
    }

    public void onAdd() {
        analyticsReporter.addFavorite(model.userId, model.userName);

        Subscription subscription = saveFavoriteUseCase.execute(model.userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorAction<>(),
                        () -> {
                            getView().hideAddButton();
                            getView().showSnackBar(R.string.message_added);
                        });
        subscriptions.add(subscription);
    }

    private Single<UserPassingModel> findUser(String userId) {
        return findUserUseCase.execute(userId)
                .subscribeOn(Schedulers.io())
                .map(user -> {
                    model.userName = user.name;
                    model.imageUri = user.imageUri;
                    model.email = user.email;
                    return model;
                });
    }

    private void showPresence(String userId) {
        Subscription subscription = findConnectionUseCase.execute(userId)
                .map(presence -> {
                    model.isConnected = presence.isConnected;
                    model.lastOnlineTime = presence.lastOnlineTime;
                    return model;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showPresence(model), new ErrorAction<>());
        subscriptions.add(subscription);
    }

    private void showTimes(String userId) {
        Subscription timesSubscription = findTimesUseCase.execute(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(times -> getView().showTimes(times), new ErrorAction<>());
        subscriptions.add(timesSubscription);
    }

    private void showLineUrl(String userId) {
        Subscription subscription = findLineLinkUseCase.execute(userId)
                .toObservable()
                .filter(entity -> entity != null)
                .map(entity -> entity.url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lineUrl -> getView().showLineUrl(lineUrl), new ErrorAction<>());
        subscriptions.add(subscription);
    }

    private void showAddFavoriteButtonIfNeeded(String userId) {
        Subscription subscription = findFavoriteUseCase.execute(userId)
                .toObservable()
                .filter(favorite -> favorite == null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entity -> getView().showAddButton(), new ErrorAction<>());
        subscriptions.add(subscription);
    }
}
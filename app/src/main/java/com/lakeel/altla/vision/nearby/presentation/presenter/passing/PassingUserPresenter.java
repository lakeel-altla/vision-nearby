package com.lakeel.altla.vision.nearby.presentation.presenter.passing;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindAllPassingTimeUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindConnectionUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindFavoriteUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindNearbyHistoryUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveFavoriteUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.UserPassingModelMapper;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PassingUserModel;
import com.lakeel.altla.vision.nearby.presentation.view.PassingUserView;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

import javax.inject.Inject;

import rx.Single;
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

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    private UserPassingModelMapper modelMapper = new UserPassingModelMapper();

    private PassingUserModel viewModel;

    private String historyId;

    private boolean isMapReadied;

    @Inject
    PassingUserPresenter() {
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public void onResume() {
        Subscription subscription = findNearbyHistoryUseCase.execute(historyId)
                .map(history -> {
                    viewModel = modelMapper.map(history);
                    return viewModel;
                })
                .flatMap(model1 -> findUser(model1.userId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(viewModel -> {
                    analyticsReporter.viewHistoryItem(viewModel.userId, viewModel.userName);

                    getView().showProfile(viewModel);
                    getView().showPassingData(viewModel);

                    if (isMapReadied) {
                        onMapReady();
                    }

                    showPresence(viewModel.userId);
                    showTimes(viewModel.userId);
                    showLineUrl(viewModel.userId);
                    showAddFavoriteButtonIfNeeded(viewModel.userId);
                }, new ErrorAction<>());
        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unSubscribe();
    }

    public void onMapReady() {
        isMapReadied = true;
        if (viewModel == null || viewModel.latitude == null || viewModel.longitude == null) {
            getView().hideLocation();
        } else {
            getView().showLocation(viewModel.latitude, viewModel.longitude);
        }
    }

    public void onAdd() {
        analyticsReporter.addFavorite(viewModel.userId, viewModel.userName);

        Subscription subscription = saveFavoriteUseCase.execute(viewModel.userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorAction<>(),
                        () -> {
                            getView().hideAddButton();
                            getView().showSnackBar(R.string.snackBar_message_added);
                        });
        subscriptions.add(subscription);
    }

    private Single<PassingUserModel> findUser(String userId) {
        return findUserUseCase.execute(userId)
                .map(user -> {
                    viewModel.userName = user.name;
                    viewModel.imageUri = user.imageUri;
                    viewModel.email = user.email;
                    return viewModel;
                });
    }

    private void showPresence(String userId) {
        Subscription subscription = findConnectionUseCase.execute(userId)
                .map(connection -> {
                    viewModel.isConnected = connection.isConnected;
                    viewModel.lastOnlineTime = connection.lastOnlineTime;
                    return viewModel;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> getView().showPresence(model), new ErrorAction<>());
        subscriptions.add(subscription);
    }

    private void showTimes(String userId) {
        Subscription timesSubscription = findAllPassingTimeUseCase.execute(userId)
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
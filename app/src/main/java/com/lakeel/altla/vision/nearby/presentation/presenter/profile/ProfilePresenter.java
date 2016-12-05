package com.lakeel.altla.vision.nearby.presentation.presenter.profile;

import com.lakeel.altla.vision.nearby.domain.usecase.FindUserUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.UserModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.ProfileView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.schedulers.Schedulers;

public final class ProfilePresenter extends BasePresenter<ProfileView> {

    @Inject
    FindUserUseCase findUserUseCase;

    @Inject
    ProfilePresenter() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfilePresenter.class);

    private UserModelMapper modelMapper = new UserModelMapper();

    String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void onActivityCreated() {
        Subscription subscription = findUserUseCase
                .execute(userId)
                .map(entity -> modelMapper.map(entity))
                .subscribeOn(Schedulers.io())
                .subscribe(model -> getView().showProfile(model),
                        e -> LOGGER.error("Failed to find user profile.", e));
        reusableCompositeSubscription.add(subscription);
    }
}

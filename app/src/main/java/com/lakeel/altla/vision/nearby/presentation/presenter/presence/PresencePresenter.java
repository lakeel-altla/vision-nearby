package com.lakeel.altla.vision.nearby.presentation.presenter.presence;

import com.lakeel.altla.vision.nearby.domain.usecase.FindPresenceUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.mapper.PresencesModelMapper;
import com.lakeel.altla.vision.nearby.presentation.view.PresenceView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;

public final class PresencePresenter extends BasePresenter<PresenceView> {

    @Inject
    FindPresenceUseCase findPresenceUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(PresencePresenter.class);

    private String userId;

    private PresencesModelMapper presencesModelMapper = new PresencesModelMapper();

    @Inject
    PresencePresenter() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void onActivityCreated() {
        Subscription subscription = findPresenceUseCase
                .execute(userId)
                .map(entity -> presencesModelMapper.map(entity))
                .subscribe(model -> getView().showPresence(model),
                        e -> LOGGER.error("Failed to find user presences.", e));
        reusableCompositeSubscription.add(subscription);
    }
}

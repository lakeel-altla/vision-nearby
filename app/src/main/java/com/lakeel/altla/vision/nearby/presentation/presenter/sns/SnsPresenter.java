package com.lakeel.altla.vision.nearby.presentation.presenter.sns;

import com.lakeel.altla.vision.nearby.core.StringUtils;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineUrlUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.SnsView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class SnsPresenter extends BasePresenter<SnsView> {

    @Inject
    FindLineUrlUseCase findLineUrlUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(SnsPresenter.class);

    private String userId;

    @Inject
    SnsPresenter() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void onActivityCreated() {
        Subscription subscription = findLineUrlUseCase
                .execute(userId)
                .map(entity -> {
                    if (entity == null) return StringUtils.EMPTY;
                    return entity.url;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lineUrl -> getView().showLineUrl(lineUrl),
                        e -> LOGGER.error("Failed to find LINE url.", e)
                );
        reusableCompositeSubscription.add(subscription);
    }
}

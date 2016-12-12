package com.lakeel.altla.vision.nearby.presentation.presenter.setting;

import com.lakeel.altla.vision.nearby.domain.usecase.FindConfigsUseCase;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.SettingView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class SettingPresenter extends BasePresenter<SettingView> {

    @Inject
    FindConfigsUseCase findConfigsUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingPresenter.class);

    @Inject
    SettingPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findConfigsUseCase
                .execute()
                .toObservable()
                .filter(entity -> entity.isCmLinkEnabled)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    getView().showCmPreferences();
                }, e -> {
                    LOGGER.error("Failed to find CM links settings.", e);
                });
        subscriptions.add(subscription);
    }
}

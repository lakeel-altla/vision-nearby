package com.lakeel.altla.vision.nearby.presentation.presenter.line;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveLineUrlUseCase;
import com.lakeel.altla.vision.nearby.presentation.constants.AnalyticsEvent;
import com.lakeel.altla.vision.nearby.presentation.constants.AnalyticsParam;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.LineSettingView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class LineSettingPresenter extends BasePresenter<LineSettingView> {

    @Inject
    FirebaseAnalytics firebaseAnalytics;

    @Inject
    SaveLineUrlUseCase saveLineUrlUseCase;

    @Inject
    FindLineLinkUseCase findLineLinkUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(LineSettingPresenter.class);

    @Inject
    LineSettingPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findLineLinkUseCase
                .execute(MyUser.getUid())
                .toObservable()
                .filter(entity -> entity != null)
                .map(entity -> entity.url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(url -> getView().showLineUrl(url),
                        e -> LOGGER.error("Failed to find LINE url.", e));
        subscriptions.add(subscription);
    }

    public void onSaveLineUrl(String url) {
        MyUser.UserData userData = MyUser.getUserData();
        Bundle bundle = new Bundle();
        bundle.putString(AnalyticsParam.USER_ID.getValue(), userData.userId);
        bundle.getString(AnalyticsParam.USER_NAME.getValue(), userData.displayName);
        firebaseAnalytics.logEvent(AnalyticsEvent.SAVE_LINE_URL.getValue(), bundle);

        Subscription subscription = saveLineUrlUseCase
                .execute(MyUser.getUid(), url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                            getView().showLineUrl(url);
                            getView().showSnackBar(R.string.message_added);
                        },
                        e -> {
                            LOGGER.error("Failed to save line URL.", e);
                            getView().showSnackBar(R.string.error_not_added);
                        });
        subscriptions.add(subscription);
    }
}

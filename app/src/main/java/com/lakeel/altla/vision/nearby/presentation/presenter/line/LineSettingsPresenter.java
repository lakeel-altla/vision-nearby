package com.lakeel.altla.vision.nearby.presentation.presenter.line;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveLineUrlUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.LineSettingsView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class LineSettingsPresenter extends BasePresenter<LineSettingsView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    SaveLineUrlUseCase saveLineUrlUseCase;

    @Inject
    FindLineLinkUseCase findLineLinkUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(LineSettingsPresenter.class);

    @Inject
    LineSettingsPresenter() {
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
        analyticsReporter.inputLineUrl();

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

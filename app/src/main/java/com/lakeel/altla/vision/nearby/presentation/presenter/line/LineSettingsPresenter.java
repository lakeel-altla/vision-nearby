package com.lakeel.altla.vision.nearby.presentation.presenter.line;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveLINEUrlUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.LineSettingsView;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class LineSettingsPresenter extends BasePresenter<LineSettingsView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    SaveLINEUrlUseCase saveLineUrlUseCase;

    @Inject
    FindLineLinkUseCase findLineLinkUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(LineSettingsPresenter.class);

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    @Inject
    LineSettingsPresenter() {
    }

    public void onActivityCreated() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Subscription subscription = findLineLinkUseCase.execute(firebaseUser.getUid())
                    .toObservable()
                    .filter(lineLink -> lineLink != null)
                    .map(lineLink -> lineLink.url)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(url -> getView().showLineUrl(url),
                            e -> {
                                LOGGER.error("Failed.", e);
                                getView().showSnackBar(R.string.snackBar_error_failed);
                            });
            subscriptions.add(subscription);

        }
    }

    public void onStop() {
        subscriptions.unSubscribe();
    }

    public void onSave(String url) {
        UrlValidator validator = UrlValidator.getInstance();
        boolean isValid = validator.isValid(url);
        if (!isValid) {
            getView().showSnackBar(R.string.snackBar_error_invalid_uri);
            return;
        }

        analyticsReporter.inputLineUrl();

        Subscription subscription = saveLineUrlUseCase.execute(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getView().showLineUrl(url);
                    getView().showSnackBar(R.string.snackBar_message_added);
                }, e -> {
                    LOGGER.error("Failed.", e);
                    getView().showSnackBar(R.string.snackBar_error_failed);
                });
        subscriptions.add(subscription);
    }
}

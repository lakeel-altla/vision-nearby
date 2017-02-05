package com.lakeel.altla.vision.nearby.presentation.presenter.line;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineLinkUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveLINEUrlUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.uri.UriChecker;
import com.lakeel.altla.vision.nearby.presentation.view.LineSettingsView;
import com.lakeel.altla.vision.nearby.rx.ErrorAction;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.lakeel.altla.vision.nearby.presentation.uri.UriChecker.Result;

public final class LineSettingsPresenter extends BasePresenter<LineSettingsView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    SaveLINEUrlUseCase saveLineUrlUseCase;

    @Inject
    FindLineLinkUseCase findLineLinkUseCase;

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    @Inject
    LineSettingsPresenter() {
    }

    public void onActivityCreated() {
        Subscription subscription = findLineLinkUseCase.execute(MyUser.getUserId())
                .toObservable()
                .filter(entity -> entity != null)
                .map(entity -> entity.url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(url -> getView().showLineUrl(url), new ErrorAction<>());
        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unSubscribe();
    }

    public void onSave(String url) {
        UriChecker checker = new UriChecker(url);
        Result result = checker.check();
        if (result == Result.SYNTAX_ERROR) {
            getView().showSnackBar(R.string.error_uri_syntax);
            return;
        }

        analyticsReporter.inputLineUrl();

        Subscription subscription = saveLineUrlUseCase.execute(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getView().showLineUrl(url);
                    getView().showSnackBar(R.string.message_added);
                }, new ErrorAction<>());
        subscriptions.add(subscription);
    }
}

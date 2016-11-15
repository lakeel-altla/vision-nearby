package com.lakeel.profile.notification.presentation.presenter.line;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.domain.usecase.FindLINEUrlUseCase;
import com.lakeel.profile.notification.domain.usecase.SaveLINEUrlUseCase;
import com.lakeel.profile.notification.presentation.firebase.MyUser;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
import com.lakeel.profile.notification.presentation.view.LineSettingsView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class LineSettingsPresenter extends BasePresenter<LineSettingsView> {

    @Inject
    SaveLINEUrlUseCase mSaveLINEUrlUseCase;

    @Inject
    FindLINEUrlUseCase mFindLINEUrlUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(LineSettingsPresenter.class);

    @Inject
    LineSettingsPresenter() {
    }

    @Override
    public void onActivityCreated() {
        Subscription subscription = mFindLINEUrlUseCase
                .execute(MyUser.getUid())
                .toObservable()
                .filter(entity -> entity != null)
                .map(entity -> entity.url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(url -> getView().showLINEUrl(url),
                        e -> LOGGER.error("Failed to find LINE url.", e));
        mCompositeSubscription.add(subscription);
    }

    public void onSaveLineUrl(String url) {
        Subscription subscription = mSaveLINEUrlUseCase
                .execute(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                            getView().showLINEUrl(url);
                            getView().showSnackBar(R.string.message_added);
                        },
                        e -> {
                            LOGGER.error("Failed to save line URL.", e);
                            getView().showSnackBar(R.string.error_not_added);
                        });

        mCompositeSubscription.add(subscription);
    }
}

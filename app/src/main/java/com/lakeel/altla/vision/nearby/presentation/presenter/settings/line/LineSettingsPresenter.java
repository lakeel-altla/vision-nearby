package com.lakeel.altla.vision.nearby.presentation.presenter.settings.line;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.FindLineUrlUseCase;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveLineUrlUseCase;
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
    SaveLineUrlUseCase mSaveLineUrlUseCase;

    @Inject
    FindLineUrlUseCase mFindLineUrlUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(LineSettingsPresenter.class);

    @Inject
    LineSettingsPresenter() {
    }

    @Override
    public void onActivityCreated() {
        Subscription subscription = mFindLineUrlUseCase
                .execute(MyUser.getUid())
                .toObservable()
                .filter(entity -> entity != null)
                .map(entity -> entity.url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(url -> getView().showLineUrl(url),
                        e -> LOGGER.error("Failed to find LINE url.", e));
        reusableCompositeSubscription.add(subscription);
    }

    public void onSaveLineUrl(String url) {
        Subscription subscription = mSaveLineUrlUseCase
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

        reusableCompositeSubscription.add(subscription);
    }
}

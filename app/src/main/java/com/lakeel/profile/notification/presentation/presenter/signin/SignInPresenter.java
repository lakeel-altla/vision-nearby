package com.lakeel.profile.notification.presentation.presenter.signin;

import com.firebase.ui.auth.AuthUI;
import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.domain.usecase.SaveItemUseCase;
import com.lakeel.profile.notification.domain.usecase.SignOutUseCase;
import com.lakeel.profile.notification.presentation.presenter.BasePresenter;
import com.lakeel.profile.notification.presentation.view.SignInView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class SignInPresenter extends BasePresenter<SignInView> {

    @Inject
    SaveItemUseCase mSaveItemUseCase;

    @Inject
    SignOutUseCase mSignOutUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(SignInPresenter.class);

    @Inject
    public SignInPresenter() {
    }

    // サービス利用規約の URL
    private static final String GOOGLE_TOS_URL =
            "https://www.google.com/policies/terms/";

    private static final String FIREBASE_TOS_URL =
            "https://www.firebase.com/terms/terms-of-service.html";

    public void onSignIn() {
        // FirebaseUI を利用。各種プロバイダを設定。
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setProviders(
                        AuthUI.EMAIL_PROVIDER,
                        AuthUI.FACEBOOK_PROVIDER,
                        AuthUI.GOOGLE_PROVIDER)
                .setTosUrl(GOOGLE_TOS_URL)
                .setTheme(R.style.FirebaseUI)
                .build();

        getView().showSignInActivity(intent);
    }

    public void onSignedIn() {
        Subscription subscription = mSaveItemUseCase
                .execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    LOGGER.error("Failed to sign in", e);
                    getView().showSnackBar(R.string.error_not_signed_in);
                    onSignOut();
                }, () -> getView().onSignedIn());

        mCompositeSubscription.add(subscription);
    }

    private void onSignOut() {
        Subscription subscription = mSignOutUseCase
                .execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(throwable -> getView().showSnackBar(R.string.error_not_signed_in),
                        () -> {
                        });

        mCompositeSubscription.add(subscription);
    }
}

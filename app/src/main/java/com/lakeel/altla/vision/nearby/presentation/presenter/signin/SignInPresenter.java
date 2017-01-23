package com.lakeel.altla.vision.nearby.presentation.presenter.signin;

import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.SaveUserUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.presenter.BasePresenter;
import com.lakeel.altla.vision.nearby.presentation.view.SignInView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class SignInPresenter extends BasePresenter<SignInView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    SaveUserUseCase saveUserUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(SignInPresenter.class);

    @Inject
    SignInPresenter() {
    }

    private static final String GOOGLE_TOS_URL =
            "https://www.google.com/policies/terms/";

    public void onActivityCreated() {
        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setProviders(AuthUI.GOOGLE_PROVIDER)
                .setTosUrl(GOOGLE_TOS_URL)
                .setTheme(R.style.FirebaseUI)
                .build();

        getView().showSignInActivity(intent);
    }

    public void onSignedIn() {
        analyticsReporter.signIn();

        Subscription subscription = saveUserUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> {
                    LOGGER.error("Failed to sign in.", e);

                    FirebaseAuth.getInstance().signOut();
                    getView().showSnackBar(R.string.error_not_signed_in);
                }, () -> getView().signInSuccess());
        subscriptions.add(subscription);
    }
}

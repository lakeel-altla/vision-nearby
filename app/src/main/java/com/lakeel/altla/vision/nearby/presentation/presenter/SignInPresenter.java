package com.lakeel.altla.vision.nearby.presentation.presenter;

import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.domain.usecase.SignInUseCase;
import com.lakeel.altla.vision.nearby.presentation.analytics.AnalyticsReporter;
import com.lakeel.altla.vision.nearby.presentation.view.SignInView;
import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class SignInPresenter extends BasePresenter<SignInView> {

    @Inject
    AnalyticsReporter analyticsReporter;

    @Inject
    SignInUseCase signInUseCase;

    private static final Logger LOGGER = LoggerFactory.getLogger(SignInPresenter.class);

    private final ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

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
                .setTheme(R.style.AuthUiTheme)
                .build();

        getView().showGoogleSignInActivity(intent);
    }

    public void onStop() {
        subscriptions.unSubscribe();
    }

    public void onSignedIn() {
        analyticsReporter.signIn();

        Subscription subscription = signInUseCase
                .execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deviceToken -> {
                    getView().onSignedIn();
                }, e -> {
                    LOGGER.error("Failed to sign in.", e);

                    FirebaseAuth.getInstance().signOut();
                    getView().showSnackBar(R.string.snackBar_error_not_signed_in);
                });
        subscriptions.add(subscription);
    }
}

package com.lakeel.altla.vision.nearby.presentation.view.fragment.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lakeel.altla.vision.nearby.R;
import com.lakeel.altla.vision.nearby.presentation.application.App;
import com.lakeel.altla.vision.nearby.presentation.presenter.signin.SignInPresenter;
import com.lakeel.altla.vision.nearby.presentation.view.SignInView;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class SignInFragment extends Fragment implements SignInView {

    @Inject
    SignInPresenter presenter;

    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;

    private static final int SIGN_IN_REQUEST_CODE = 1;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Dagger
        MainActivity.getUserComponent(this).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);

        presenter.onCreateView(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(R.string.title_sign_in);

        presenter.onActivityCreated();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (SIGN_IN_REQUEST_CODE == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                presenter.onSignedIn();
            } else {
                Snackbar.make(mainLayout, R.string.error_not_signed_in, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void showSignInActivity(Intent intent) {
        startActivityForResult(intent, SIGN_IN_REQUEST_CODE);
    }

    @Override
    public void signInSuccess() {
        App.startSubscribeInBackground(this);
        ((MainActivity) getActivity()).postSignIn();
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(mainLayout, R.string.error_not_signed_in, Snackbar.LENGTH_SHORT).show();
    }
}

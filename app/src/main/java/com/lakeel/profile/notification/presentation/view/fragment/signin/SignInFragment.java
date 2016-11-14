package com.lakeel.profile.notification.presentation.view.fragment.signin;

import com.lakeel.profile.notification.R;
import com.lakeel.profile.notification.presentation.presenter.signin.SignInPresenter;
import com.lakeel.profile.notification.presentation.view.SignInView;
import com.lakeel.profile.notification.presentation.view.activity.MainActivity;

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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class SignInFragment extends Fragment implements SignInView {

    //
    // Fragment 再生成時、リフレクションでデフォルトコンストラクタが
    // 呼ばれるため、デフォルトコンストラクタは public で残しておかなければならない。
    //

    @Inject
    SignInPresenter mPresenter;

    @BindView(R.id.layout)
    RelativeLayout mRelativeLayout;

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

        mPresenter.onCreateView(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @OnClick(R.id.button_signIn)
    public void onSignInButtonClick(View view) {
        mPresenter.onSignIn();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (SIGN_IN_REQUEST_CODE == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                mPresenter.onSignedIn();
            } else {
                Snackbar.make(mRelativeLayout, R.string.error_not_signed_in, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void showSignInActivity(Intent intent) {
        startActivityForResult(intent, SIGN_IN_REQUEST_CODE);
    }

    @Override
    public void showTitle(String title) {
        getActivity().setTitle(title);
    }

    @Override
    public void onSignedIn() {
        ((MainActivity) getActivity()).onSignedIn();
    }

    @Override
    public void showSnackBar(int resId) {
        Snackbar.make(mRelativeLayout, R.string.error_not_signed_in, Snackbar.LENGTH_SHORT).show();
    }
}

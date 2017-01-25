package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.firebase.MyUser;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.ActivityModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserModel;

public interface ActivityView {

    void showFavoriteListFragment();

    void showSignInFragment();

    void showDrawerProfile(MyUser.UserProfile userProfile);

    void updateProfile(ActivityModel model);

    void showSnackBar(@StringRes int resId);

    void showAdvertiseDisableConfirmDialog();

    void showBleEnabledActivity();

    void startDetectBeaconsInBackground();

    void stopDetectBeaconsInBackground();

    void startAdvertise(String beaconId);
}

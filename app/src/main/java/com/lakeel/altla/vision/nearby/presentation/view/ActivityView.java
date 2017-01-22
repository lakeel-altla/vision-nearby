package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserModel;

public interface ActivityView {

    void showFavoriteListFragment();

    void showSignInFragment();

    void showProfile(String displayName, String email, String imageUri);

    void updateProfile(UserModel model);

    void showSnackBar(@StringRes int resId);

    void showAdvertiseDisableConfirmDialog();

    void showBleEnabledActivity();

    void stopMonitorBeacons();

    void startAdvertise(String beaconId);
}

package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.ActivityModel;

public interface ActivityView {

    void showFavoriteListFragment();

    void showSignInFragment();

    void showDrawerHeaderProfile(ActivityModel model);

    void showSnackBar(@StringRes int resId);

    void showAdvertiseDisableConfirmDialog();

    void showBleEnabledActivity();

    void requestAccessFineLocationPermission();

    void startDetectBeaconsInBackground();

    void stopDetectBeaconsInBackground();

    void startAdvertiseInBackground(String beaconId);

    void finishActivity();
}

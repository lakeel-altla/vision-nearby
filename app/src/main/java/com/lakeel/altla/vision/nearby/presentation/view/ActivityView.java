package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.ActivityModel;

public interface ActivityView {

    void showFavoriteListFragment();

    void showSignInFragment();

    void updateDrawerProfile(@NonNull ActivityModel model);

    void showAdvertiseDisableConfirmDialog();

    void showBleEnabledActivity();

    void requestAccessFineLocationPermission();

    void startDetectBeaconsInBackground();

    void stopDetectBeaconsInBackground();

    void startAdvertiseInBackground(@NonNull String beaconId);

    void finishActivity();

    void showSnackBar(@StringRes int resId);
}

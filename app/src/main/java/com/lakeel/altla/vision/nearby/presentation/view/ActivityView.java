package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;

public interface ActivityView extends BaseView {

    void showFavoriteListFragment();

    void showSignInFragment();

    void showProfile(String displayName, String email, String imageUri);

    void showSnackBar(@StringRes int resId);

    void showAdvertiseDisableConfirmDialog();

    void showBleEnabledActivity();

    void startMonitorBeacons();

    void stopMonitorBeacons();

    void startAdvertiseService(String beaconId);
}

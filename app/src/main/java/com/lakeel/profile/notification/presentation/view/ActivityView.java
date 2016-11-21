package com.lakeel.profile.notification.presentation.view;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;

import com.lakeel.profile.notification.presentation.presenter.model.PreferenceModel;

import android.support.annotation.StringRes;

public interface ActivityView extends BaseView {

    void showConnectedResolutionSystemDialog(ConnectionResult connectionResult);

    void showFavoritesListFragment();

    void showSignInFragment();

    void showProfile(String displayName, String email, String imageUri);

    void showSnackBar(@StringRes int resId);

    void showPublishDisableDialog();

    void showBleEnabledActivity();

    void startPublishService(PreferenceModel model);

    void showAccessFineLocationPermissionSystemDialog();

    void showResolutionSystemDialog(Status status);
}

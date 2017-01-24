package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteUserModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PresenceModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserModel;

import java.util.ArrayList;

public interface FavoriteUserView {

    void showSnackBar(@StringRes int resId);

    void showPresence(FavoriteUserModel model);

    void showProfile(FavoriteUserModel model);

    void showLineUrl(FavoriteUserModel url);

    void showDistanceEstimationFragment(ArrayList<String> beaconIds, String targetName);
}

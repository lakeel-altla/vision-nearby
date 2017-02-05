package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.FavoriteUserModel;

import java.util.ArrayList;

public interface FavoriteUserView {

    void showSnackBar(@StringRes int resId);

    void showPresence(FavoriteUserModel model);

    void showProfile(FavoriteUserModel model);

    void showTimes(long times);

    void showPassingData(FavoriteUserModel model);

    void showLocation(String latitude, String longitude);

    void hideLocation();

    void showLineUrl(String url);

    void showDistanceEstimationFragment(ArrayList<String> beaconIds, String targetName);
}

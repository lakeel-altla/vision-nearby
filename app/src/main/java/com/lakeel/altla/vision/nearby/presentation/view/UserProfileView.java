package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.PresenceModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserModel;

import java.util.ArrayList;

public interface UserProfileView {

    void showSnackBar(@StringRes int resId);

    void showPresence(PresenceModel model);

    void showProfile(UserModel model);

    void showLineUrl(String url);

    void showDistanceEstimationFragment(ArrayList<String> beaconIds, String targetName);
}

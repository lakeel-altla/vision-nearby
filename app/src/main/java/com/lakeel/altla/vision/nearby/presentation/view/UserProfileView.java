package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PresenceModel;

import android.support.annotation.StringRes;

import java.util.ArrayList;

public interface UserProfileView {

    void showSnackBar(@StringRes int resId);

    void showPresence(PresenceModel model);

    void showProfile(UserModel model);

    void showShareSheet();

    void initializeOptionMenu();

    void showLineUrl(String url);

    void showMessageInputDialog();

    void showFindNearbyDeviceFragment(ArrayList<String> beaconIds, String targetName);
}

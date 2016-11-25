package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.ItemModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PresenceModel;

import android.support.annotation.StringRes;

import java.util.ArrayList;

public interface ProfileView {

    void showSnackBar(@StringRes int resId);

    void showPresence(PresenceModel model);

    void showProfile(ItemModel model);

    void showShareSheet();

    void initializeOptionMenu();

    void showLineUrl(String url);

    void showFindNearbyDeviceFragment(ArrayList<String> beaconIds, String targetName);
}

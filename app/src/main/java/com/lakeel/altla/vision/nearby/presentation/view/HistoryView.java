package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.UserModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.PresenceModel;

public interface HistoryView {

    void showSnackBar(@StringRes int resId);

    void showPresence(PresenceModel model);

    void showLocationMap(String latitude, String longitude);

    void hideLocation();

    void showTimes(long times);

    void showProfile(UserModel model);

    void showAddButton();

    void hideAddButton();

    void showLineUrl(String url);
}

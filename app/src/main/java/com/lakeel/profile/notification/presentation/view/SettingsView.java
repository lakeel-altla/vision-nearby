package com.lakeel.profile.notification.presentation.view;

import com.lakeel.profile.notification.presentation.presenter.model.BeaconModel;
import com.lakeel.profile.notification.presentation.presenter.model.CMLinksModel;

import android.support.annotation.StringRes;

public interface SettingsView extends BaseView {

    void showTitle(@StringRes int resId);

    void disablePublishSettings();

    void startPublishInService(BeaconModel model);

    void showLINEUrl(String url);

    void showCMPreferences(CMLinksModel model);

    void updateCMApiKeyPreference(String apiKey);

    void updateCMSecretKeyPreference(String secretKey);

    void updateCMJidPreference(String jid);

    void showSnackBar(@StringRes int resId);
}

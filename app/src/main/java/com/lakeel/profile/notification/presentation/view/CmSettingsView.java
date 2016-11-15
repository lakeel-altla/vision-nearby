package com.lakeel.profile.notification.presentation.view;

import com.lakeel.profile.notification.presentation.presenter.model.CMLinksModel;

import android.support.annotation.StringRes;

public interface CmSettingsView {

    void showCMPreferences(CMLinksModel model);

    void updateCMApiKeyPreference(String apiKey);

    void updateCMSecretKeyPreference(String secretKey);

    void updateCMJidPreference(String jid);

    void showSnackBar(@StringRes int resId);
}

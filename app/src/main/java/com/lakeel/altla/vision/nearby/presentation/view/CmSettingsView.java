package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.CMLinksModel;

import android.support.annotation.StringRes;

public interface CmSettingsView {

    void showCMPreferences(CMLinksModel model);

    void updateCMApiKeyPreference(String apiKey);

    void updateCMSecretKeyPreference(String secretKey);

    void updateCMJidPreference(String jid);

    void showSnackBar(@StringRes int resId);
}

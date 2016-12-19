package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.CmLinkModel;

import android.support.annotation.StringRes;

public interface CmSettingView {

    void showCmPreferences(CmLinkModel model);

    void updateCmApiKeyPreference(String apiKey);

    void updateCmSecretKeyPreference(String secretKey);

    void updateCmJidPreference(String jid);

    void showSnackBar(@StringRes int resId);
}

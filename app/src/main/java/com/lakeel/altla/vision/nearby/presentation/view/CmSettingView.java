package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.CmLinksModel;

import android.support.annotation.StringRes;

public interface CmSettingView {

    void showCmPreferences(CmLinksModel model);

    void updateCmApiKeyPreference(String apiKey);

    void updateCmSecretKeyPreference(String secretKey);

    void updateCmJidPreference(String jid);

    void showSnackBar(@StringRes int resId);
}

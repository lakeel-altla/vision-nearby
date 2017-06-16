package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;

public interface InformationView {

    void showInformation(@NonNull InformationModel model);

    void showSnackBar(@StringRes int resId);
}

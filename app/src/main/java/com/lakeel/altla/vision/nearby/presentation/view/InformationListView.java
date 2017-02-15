package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

public interface InformationListView {

    void updateItems();

    void showInformationFragment(String informationId);

    void showSnackBar(@StringRes int resId);
}

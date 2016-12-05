package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.intent.HistoryBundleData;

import android.support.annotation.StringRes;

public interface HistoryListView extends BaseView {

    void updateItems();

    void showSnackBar(@StringRes int resId);

    void showHistoryFragment(HistoryBundleData data);
}

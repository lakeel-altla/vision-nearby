package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.view.bundle.HistoryBundle;

import android.support.annotation.StringRes;

public interface HistoryListView extends BaseView {

    void updateItems();

    void removeAll(int size);

    void showSnackBar(@StringRes int resId);

    void showHistoryFragment(HistoryBundle data);
}

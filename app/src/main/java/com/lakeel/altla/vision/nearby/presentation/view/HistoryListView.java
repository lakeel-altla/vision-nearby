package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.view.bundle.HistoryBundle;

public interface HistoryListView {

    void updateItems();

    void removeAll(int size);

    void showEmptyView();

    void hideEmptyView();

    void showSnackBar(@StringRes int resId);

    void showHistoryFragment(HistoryBundle data);
}

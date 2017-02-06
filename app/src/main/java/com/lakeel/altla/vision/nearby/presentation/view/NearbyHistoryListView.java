package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.StringRes;

public interface NearbyHistoryListView {

    void updateItems();

    void removeAll(int size);

    void showEmptyView();

    void hideEmptyView();

    void showSnackBar(@StringRes int resId);

    void showPassingUserFragment(String historyId);
}
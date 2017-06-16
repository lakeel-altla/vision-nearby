package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.lakeel.altla.vision.nearby.presentation.view.fragment.bundle.PassingUser;

public interface NearbyHistoryListView {

    void updateItems();

    void removeAll(int size);

    void showEmptyView();

    void hideEmptyView();

    void showPassingUserFragment(@NonNull PassingUser passingUser);

    void showSnackBar(@StringRes int resId);
}

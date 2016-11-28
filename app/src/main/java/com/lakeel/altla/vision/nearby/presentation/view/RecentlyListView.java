package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.intent.RecentlyIntentData;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.RecentlyItemModel;

import android.support.annotation.StringRes;

public interface RecentlyListView extends BaseView {

    void updateItems();

    void showSnackBar(@StringRes int resId);

    void showRecentlyUserActivity(RecentlyIntentData data);
}

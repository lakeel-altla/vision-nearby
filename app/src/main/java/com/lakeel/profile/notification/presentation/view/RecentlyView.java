package com.lakeel.profile.notification.presentation.view;

import com.lakeel.profile.notification.presentation.intent.RecentlyIntentData;
import com.lakeel.profile.notification.presentation.presenter.model.RecentlyItemModel;

import android.support.annotation.StringRes;

public interface RecentlyView extends BaseView {

    void updateItems();

    void showSnackBar(@StringRes int resId);

    void showRecentlyUserActivity(RecentlyIntentData data);
}

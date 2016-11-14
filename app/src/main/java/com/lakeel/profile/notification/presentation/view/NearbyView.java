package com.lakeel.profile.notification.presentation.view;

import com.google.android.gms.common.api.Status;

public interface NearbyView extends BaseView {

    void showTitle(int resId);

    void updateItems();

    void showSnackBar(int resId);

    void showIndicator();

    void hideIndicator();

    void drawEditableActionBarColor();

    void drawNormalActionBarColor();

    void showOptionMenu();

    void hideOptionMenu();

    void showShareSheet();

    void showResolutionSystemDialog(Status status);
}

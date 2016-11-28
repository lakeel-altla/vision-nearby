package com.lakeel.altla.vision.nearby.presentation.view;

import com.google.android.gms.common.api.Status;

public interface NearbyListView extends BaseView {

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

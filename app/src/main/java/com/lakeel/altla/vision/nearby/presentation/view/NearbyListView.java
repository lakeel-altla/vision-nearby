package com.lakeel.altla.vision.nearby.presentation.view;

import android.content.Intent;

public interface NearbyListView {

    void showBleEnabledActivity(Intent intent);

    void updateItems();

    void showSnackBar(int resId);

    void showIndicator();

    void hideIndicator();

    void drawEditableActionBarColor();

    void drawNormalActionBarColor();

    void hideOptionMenu();
}

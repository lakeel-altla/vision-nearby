package com.lakeel.altla.vision.nearby.presentation.view;

import android.content.Intent;

public interface NearbyUserListView {

    void showBleEnabledActivity(Intent intent);

    void updateItems();

    void showIndicator();

    void hideIndicator();

    void drawEditableActionBarColor();

    void drawDefaultActionBarColor();

    void hideOptionMenu();

    void showSnackBar(int resId);
}

package com.lakeel.altla.vision.nearby.presentation.view;

import android.content.Intent;

public interface NearbyUserListView {

    void showBleEnabledActivity(Intent intent);

    void requestAccessFineLocationPermission();

    void updateItems();

    void showIndicator();

    void hideIndicator();

    void drawDefaultActionBarColor();

    void showSnackBar(int resId);
}

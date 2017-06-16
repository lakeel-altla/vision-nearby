package com.lakeel.altla.vision.nearby.presentation.view;

import android.content.Intent;
import android.support.annotation.NonNull;

public interface NearbyUserListView {

    void showBleEnabledActivity(@NonNull Intent intent);

    void requestAccessFineLocationPermission();

    void updateItems();

    void showIndicator();

    void hideIndicator();

    void showSnackBar(int resId);
}

package com.lakeel.altla.vision.nearby.presentation.view;

import com.google.android.gms.common.api.Status;

public interface FindNearbyDeviceView {

    void showDistance(String meters);

    void showResolutionSystemDialog(Status status);
}

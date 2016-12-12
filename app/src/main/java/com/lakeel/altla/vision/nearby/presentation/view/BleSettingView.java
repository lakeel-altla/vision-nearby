package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.BeaconIdModel;

public interface BleSettingView {

    void startAdvertise(BeaconIdModel model);

    void disableAdvertiseSettings();
}

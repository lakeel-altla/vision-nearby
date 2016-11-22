package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.BeaconIdModel;

public interface BleSettingsView {

    void startPublish(BeaconIdModel model);

    void disablePublishSettings();
}

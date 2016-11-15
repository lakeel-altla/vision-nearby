package com.lakeel.profile.notification.presentation.view;

import com.lakeel.profile.notification.presentation.presenter.model.BeaconIdModel;

public interface BleSettingsView {

    void startPublishInService(BeaconIdModel model);

    void disablePublishSettings();
}

package com.lakeel.profile.notification.presentation.view;

import com.lakeel.profile.notification.presentation.presenter.device.DeviceListPresenter;
import com.lakeel.profile.notification.presentation.presenter.model.DeviceModel;

public interface DeviceItemView extends ItemView {

    void setItemPresenter(DeviceListPresenter.DeviceItemPresenter itemPresenter);

    void showItem(DeviceModel model);
}

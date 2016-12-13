package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.setting.device.DeviceListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.DeviceModel;

public interface DeviceItemView extends ItemView {

    void setItemPresenter(DeviceListPresenter.DeviceItemPresenter itemPresenter);

    void showItem(DeviceModel model);
}

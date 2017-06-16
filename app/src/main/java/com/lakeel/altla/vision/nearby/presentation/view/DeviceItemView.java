package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.presentation.presenter.DeviceListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.DeviceModel;

public interface DeviceItemView extends ItemView {

    void setItemPresenter(@NonNull DeviceListPresenter.DeviceItemPresenter itemPresenter);

    void showItem(@NonNull DeviceModel model);

}

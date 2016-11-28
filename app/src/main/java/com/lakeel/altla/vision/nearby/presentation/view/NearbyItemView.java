package com.lakeel.altla.vision.nearby.presentation.view;


import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyItemModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.nearby.NearbyListPresenter;

public interface NearbyItemView extends ItemView {

    void setItemPresenter(NearbyListPresenter.NearbyItemPresenter itemPresenter);

    void showItem(NearbyItemModel model);
}
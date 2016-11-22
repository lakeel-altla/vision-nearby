package com.lakeel.altla.vision.nearby.presentation.view;


import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyItemModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.nearby.NearbyPresenter;

public interface NearbyItemView extends ItemView {

    void setItemPresenter(NearbyPresenter.NearbyItemPresenter itemPresenter);

    void showItem(NearbyItemModel model);
}
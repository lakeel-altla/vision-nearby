package com.lakeel.altla.vision.nearby.presentation.view;


import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyUserModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.nearby.NearbyUserListPresenter;

public interface NearbyUserItemView extends ItemView {

    void setItemPresenter(NearbyUserListPresenter.NearbyUserItemPresenter itemPresenter);

    void showItem(NearbyUserModel model);
}
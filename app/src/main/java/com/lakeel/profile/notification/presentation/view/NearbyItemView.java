package com.lakeel.profile.notification.presentation.view;


import com.lakeel.profile.notification.presentation.presenter.model.NearbyItemsModel;
import com.lakeel.profile.notification.presentation.presenter.nearby.NearbyPresenter;

public interface NearbyItemView extends ItemView {

    void setItemPresenter(NearbyPresenter.NearbyItemPresenter itemPresenter);

    void showItem(NearbyItemsModel model);
}
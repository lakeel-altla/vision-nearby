package com.lakeel.altla.vision.nearby.presentation.view;


import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.presentation.presenter.NearbyUserListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyUserModel;

public interface NearbyUserItemView extends ItemView {

    void setItemPresenter(@NonNull NearbyUserListPresenter.NearbyUserItemPresenter itemPresenter);

    void showItem(@NonNull NearbyUserModel model);
}
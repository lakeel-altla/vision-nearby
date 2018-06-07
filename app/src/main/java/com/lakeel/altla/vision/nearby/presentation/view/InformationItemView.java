package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.presentation.presenter.InformationListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;

public interface InformationItemView extends ItemView {

    void setItemPresenter(@NonNull InformationListPresenter.InformationItemPresenter itemPresenter);

    void showItem(@NonNull InformationModel model);
}

package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.InformationListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;

public interface InformationItemView extends ItemView {

    void setItemPresenter(InformationListPresenter.InformationItemPresenter itemPresenter);

    void showItem(InformationModel model);
}

package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.information.InformationPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.InformationModel;

public interface InformationItemView extends ItemView {

    void setItemPresenter(InformationPresenter.InformationItemPresenter itemPresenter);

    void showItem(InformationModel model);
}

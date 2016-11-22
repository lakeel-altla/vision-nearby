package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.RecentlyItemModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.recently.RecentlyPresenter;

public interface RecentlyItemView extends ItemView {

    void setItemPresenter(RecentlyPresenter.RecentlyItemPresenter itemPresenter);

    void showItem(RecentlyItemModel model);

    void closeItem();
}

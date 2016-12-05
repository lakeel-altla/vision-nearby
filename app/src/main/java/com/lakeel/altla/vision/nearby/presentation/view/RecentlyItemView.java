package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.RecentlyModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.recently.RecentlyListPresenter;

public interface RecentlyItemView extends ItemView {

    void setItemPresenter(RecentlyListPresenter.RecentlyItemPresenter itemPresenter);

    void showItem(RecentlyModel model);

    void closeItem();
}

package com.lakeel.profile.notification.presentation.view;

import com.lakeel.profile.notification.presentation.presenter.model.RecentlyItemModel;
import com.lakeel.profile.notification.presentation.presenter.recently.RecentlyPresenter;

public interface RecentlyItemView extends ItemView {

    void setItemPresenter(RecentlyPresenter.RecentlyItemPresenter itemPresenter);

    void showItem(RecentlyItemModel model);

    void closeItem();
}

package com.lakeel.profile.notification.presentation.view;

import com.lakeel.profile.notification.presentation.presenter.model.ItemModel;
import com.lakeel.profile.notification.presentation.presenter.search.SearchPresenter;

public interface SearchItemView extends ItemView {

    void setItemPresenter(SearchPresenter.SearchItemPresenter itemPresenter);

    void showItem(ItemModel model);
}

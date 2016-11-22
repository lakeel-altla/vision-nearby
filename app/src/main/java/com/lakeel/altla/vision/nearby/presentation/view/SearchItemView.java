package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.model.ItemModel;
import com.lakeel.altla.vision.nearby.presentation.presenter.search.SearchPresenter;

public interface SearchItemView extends ItemView {

    void setItemPresenter(SearchPresenter.SearchItemPresenter itemPresenter);

    void showItem(ItemModel model);
}

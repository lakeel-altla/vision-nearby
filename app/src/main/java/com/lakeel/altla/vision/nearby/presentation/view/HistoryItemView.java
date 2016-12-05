package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.history.HistoryListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.HistoryModel;

public interface HistoryItemView extends ItemView {

    void setItemPresenter(HistoryListPresenter.HistoryItemPresenter itemPresenter);

    void showItem(HistoryModel model);

    void closeItem();
}

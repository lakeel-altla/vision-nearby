package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.history.HistoryPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.HistoryModel;

public interface HistoryItemView extends ItemView {

    void setItemPresenter(HistoryPresenter.HistoryItemPresenter itemPresenter);

    void showItem(HistoryModel model);
}

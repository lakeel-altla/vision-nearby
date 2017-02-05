package com.lakeel.altla.vision.nearby.presentation.view;

import com.lakeel.altla.vision.nearby.presentation.presenter.history.NearbyHistoryListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyHistoryModel;

public interface NearbyHistoryItemView extends ItemView {

    void setItemPresenter(NearbyHistoryListPresenter.HistoryItemPresenter itemPresenter);

    void showItem(NearbyHistoryModel model);
}

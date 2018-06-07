package com.lakeel.altla.vision.nearby.presentation.view;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.presentation.presenter.NearbyHistoryListPresenter;
import com.lakeel.altla.vision.nearby.presentation.presenter.model.NearbyHistoryModel;

public interface NearbyHistoryItemView extends ItemView {

    void setItemPresenter(@NonNull NearbyHistoryListPresenter.HistoryItemPresenter itemPresenter);

    void showItem(@NonNull NearbyHistoryModel model);
}
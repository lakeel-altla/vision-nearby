package com.lakeel.altla.vision.nearby.presentation.view;

public interface InformationListView {

    void updateItems();

    void showEmptyView();

    void hideEmptyView();

    void showInformationFragment(String informationId);
}

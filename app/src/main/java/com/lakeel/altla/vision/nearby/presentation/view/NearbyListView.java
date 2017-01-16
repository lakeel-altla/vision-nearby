package com.lakeel.altla.vision.nearby.presentation.view;

public interface NearbyListView extends BaseView {

    void updateItems();

    void showSnackBar(int resId);

    void showIndicator();

    void hideIndicator();

    void drawEditableActionBarColor();

    void drawNormalActionBarColor();

    void showOptionMenu();

    void hideOptionMenu();

    void showShareSheet();
}

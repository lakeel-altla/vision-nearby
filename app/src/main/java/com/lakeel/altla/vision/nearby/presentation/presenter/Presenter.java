package com.lakeel.altla.vision.nearby.presentation.presenter;


import com.lakeel.altla.vision.nearby.presentation.view.BaseView;

public interface Presenter<V extends BaseView> {

    V getView();

    void onCreateView(V view);

    void onResume();

    void onPause();
}

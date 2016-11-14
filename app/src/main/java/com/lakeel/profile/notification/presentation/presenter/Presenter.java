package com.lakeel.profile.notification.presentation.presenter;


import com.lakeel.profile.notification.presentation.view.BaseView;

public interface Presenter<V extends BaseView> {

    V getView();

    void onCreateView(V view);

    void onResume();

    void onPause();
}

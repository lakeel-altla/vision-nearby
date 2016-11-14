package com.lakeel.profile.notification.presentation.presenter;

public class BaseAdapterPresenter<V> {

    private V mV;

    protected void setAdapterView(V v) {
        mV = v;
    }

    protected V getAdapterView() {
        return mV;
    }
}

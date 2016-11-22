package com.lakeel.altla.vision.nearby.presentation.presenter;

public class BaseAdapterPresenter<V> {

    private V mV;

    protected void setAdapterView(V v) {
        mV = v;
    }

    protected V getAdapterView() {
        return mV;
    }
}

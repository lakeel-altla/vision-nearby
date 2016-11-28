package com.lakeel.altla.vision.nearby.presentation.presenter;

import android.support.annotation.CallSuper;

public class BasePresenter<V> {

    protected ReusableCompositeSubscription reusableCompositeSubscription = new ReusableCompositeSubscription();

    protected V mV;

    protected V getView() {
        return mV;
    }

    @CallSuper
    public void onCreateView(V v) {
        mV = v;
    }

    public void onActivityCreated() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onStart() {
    }

    @CallSuper
    public void onStop() {
        reusableCompositeSubscription.unsubscribe();
    }
}

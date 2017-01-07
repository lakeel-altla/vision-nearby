package com.lakeel.altla.vision.nearby.presentation.presenter;

import android.support.annotation.CallSuper;

import com.lakeel.altla.vision.nearby.rx.ReusableCompositeSubscription;

public class BasePresenter<V> {

    protected ReusableCompositeSubscription subscriptions = new ReusableCompositeSubscription();

    protected V view;

    protected V getView() {
        return view;
    }

    @CallSuper
    public void onCreateView(V v) {
        view = v;
    }

    @CallSuper
    public void onStop() {
        subscriptions.unSubscribe();
    }
}

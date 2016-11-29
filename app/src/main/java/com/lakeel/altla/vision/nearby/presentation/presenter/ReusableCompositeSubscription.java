package com.lakeel.altla.vision.nearby.presentation.presenter;

import android.support.annotation.NonNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public final class ReusableCompositeSubscription {

    private CompositeSubscription compositeSubscription;

    public void add(@NonNull Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    public void unSubscribe() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
    }
}

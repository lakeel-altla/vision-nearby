package com.lakeel.altla.vision.nearby.presentation.presenter;

import com.lakeel.altla.vision.nearby.presentation.view.ItemView;

import android.support.annotation.NonNull;

public abstract class BaseItemPresenter<IV extends ItemView> implements ItemPresenter<IV> {

    private IV itemView;

    @Override
    public void onCreateItemView(@NonNull IV itemView) {
        this.itemView = itemView;
    }

    public IV getItemView() {
        return itemView;
    }
}

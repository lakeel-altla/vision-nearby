package com.lakeel.altla.vision.nearby.presentation.view.keyboard;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class KeyboardManager {

    private final View view;

    private final InputMethodManager inputMethodManager;

    public KeyboardManager(Context context, View view) {
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        this.view = view;
    }

    public void hide() {
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

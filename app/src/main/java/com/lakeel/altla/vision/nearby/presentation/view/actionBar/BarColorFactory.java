package com.lakeel.altla.vision.nearby.presentation.view.actionBar;

import android.os.Build;
import android.support.v4.app.Fragment;

import com.lakeel.altla.vision.nearby.R;

public final class BarColorFactory {

    private BarColorFactory() {
    }

    public static BarColor createDefaultColor(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= 21) {
            return new ToolBarColor(fragment.getActivity(), R.color.colorPrimary, R.color.colorPrimaryDark);
        } else {
            return new ActionBarColor(fragment.getActivity(), R.color.colorPrimary);
        }
    }

    public static BarColor createEditableColor(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= 21) {
            return new ToolBarColor(fragment.getActivity(), R.color.darkgray, R.color.darkgray);
        } else {
            return new ActionBarColor(fragment.getActivity(), R.color.darkgray);
        }
    }
}

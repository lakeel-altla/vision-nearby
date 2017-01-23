package com.lakeel.altla.vision.nearby.presentation.view.actionBar;

import android.os.Build;
import android.support.v4.app.Fragment;

import com.lakeel.altla.vision.nearby.R;

public final class DefaultBarColorFactory {

    private DefaultBarColorFactory() {
    }

    public static BarColor create(Fragment fragment) {
        BarColor barColor;
        if (Build.VERSION.SDK_INT >= 21) {
            barColor = new ToolBarColor(fragment.getActivity(), R.color.colorPrimary, R.color.colorPrimaryDark);
        } else {
            barColor = new ActionBarColor(fragment.getActivity(), R.color.colorPrimary);
        }
        return barColor;
    }
}

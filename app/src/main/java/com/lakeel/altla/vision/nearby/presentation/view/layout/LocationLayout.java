package com.lakeel.altla.vision.nearby.presentation.view.layout;

import com.lakeel.altla.vision.nearby.R;

import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;

public final class LocationLayout {

    @BindView(R.id.layout)
    public LinearLayout mLayout;

    @BindView(R.id.body)
    public TextView mLocation;
}

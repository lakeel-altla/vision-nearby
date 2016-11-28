package com.lakeel.altla.vision.nearby.presentation.view.layout;

import com.lakeel.altla.vision.nearby.R;

import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;

public final class PassingLayout {

    @BindView(R.id.dateText)
    public TextView dateText;

    @BindView(R.id.weatherText)
    public TextView weatherText;

    @BindView(R.id.detectedActivityText)
    public TextView detectedActivityText;

    @BindView(R.id.timesText)
    public TextView timesText;

    @BindView(R.id.locationLayout)
    public LinearLayout locationLayout;

    @BindView(R.id.locationText)
    public TextView locationText;
}

package com.lakeel.altla.vision.nearby.presentation.view.layout;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;

import butterknife.BindView;

public final class PassingLayout {

    @BindView(R.id.dateText)
    public TextView textViewDate;

    @BindView(R.id.weatherText)
    public TextView textViewWeather;

    @BindView(R.id.detectedActivityText)
    public TextView textViewDetectedActivity;

    @BindView(R.id.timesText)
    public TextView textViewTimes;

    @BindView(R.id.locationLayout)
    public LinearLayout locationLayout;
}

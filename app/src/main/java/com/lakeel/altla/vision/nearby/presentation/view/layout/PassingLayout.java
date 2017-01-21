package com.lakeel.altla.vision.nearby.presentation.view.layout;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;

import butterknife.BindView;

public final class PassingLayout {

    @BindView(R.id.textViewPassingTime)
    public TextView textViewDate;

    @BindView(R.id.textViewWeather)
    public TextView textViewWeather;

    @BindView(R.id.textViewUserActivity)
    public TextView textViewUserActivity;

    @BindView(R.id.textViewTimes)
    public TextView textViewTimes;

    @BindView(R.id.locationLayout)
    public LinearLayout locationLayout;
}

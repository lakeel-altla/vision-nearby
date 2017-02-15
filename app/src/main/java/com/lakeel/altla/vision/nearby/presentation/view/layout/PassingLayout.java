package com.lakeel.altla.vision.nearby.presentation.view.layout;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;

import butterknife.BindView;

public final class PassingLayout {

    @BindView(R.id.textViewPassingTime)
    public TextView passingTimeTextView;

    @BindView(R.id.textViewWeather)
    public TextView weatherTextView;

    @BindView(R.id.textViewUserActivity)
    public TextView userActivityTextView;

    @BindView(R.id.textViewTimes)
    public TextView timesTextView;

    @BindView(R.id.layoutLocationMap)
    public LinearLayout locationMapLayout;

    @BindView(R.id.textViewUnknown)
    public TextView unknownTextView;
}

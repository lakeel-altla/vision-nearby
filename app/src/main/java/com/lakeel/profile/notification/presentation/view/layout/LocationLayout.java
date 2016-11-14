package com.lakeel.profile.notification.presentation.view.layout;

import com.lakeel.profile.notification.R;

import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;

public final class LocationLayout {

    @BindView(R.id.layout)
    public LinearLayout mLayout;

    @BindView(R.id.body)
    public TextView mLocation;
}

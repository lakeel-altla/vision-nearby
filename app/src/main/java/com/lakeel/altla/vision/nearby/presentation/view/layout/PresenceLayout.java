package com.lakeel.altla.vision.nearby.presentation.view.layout;

import com.lakeel.altla.vision.nearby.R;

import android.widget.TextView;

import butterknife.BindView;

public final class PresenceLayout {

    @BindView(R.id.presenceText)
    public TextView presenceText;

    @BindView(R.id.lastOnlineText)
    public TextView lastOnlineText;
}

package com.lakeel.altla.vision.nearby.presentation.view.layout;

import android.widget.TextView;

import com.lakeel.altla.vision.nearby.R;

import butterknife.BindView;

public final class PresenceLayout {

    @BindView(R.id.presenceText)
    public TextView textViewPresence;

    @BindView(R.id.lastOnlineText)
    public TextView textViewLastOnline;
}

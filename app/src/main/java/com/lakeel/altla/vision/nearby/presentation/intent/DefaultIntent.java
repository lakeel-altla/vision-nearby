package com.lakeel.altla.vision.nearby.presentation.intent;

import android.content.Intent;

public final class DefaultIntent extends Intent {

    public DefaultIntent() {
        setAction(Intent.ACTION_VIEW);
        setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}

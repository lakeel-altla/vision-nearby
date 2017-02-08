package com.lakeel.altla.vision.nearby.presentation.view.intent;

import android.content.Intent;

public final class DefaultIntent extends Intent {

    public DefaultIntent() {
        setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}

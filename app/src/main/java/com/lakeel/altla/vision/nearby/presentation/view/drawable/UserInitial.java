package com.lakeel.altla.vision.nearby.presentation.view.drawable;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.amulyakhare.textdrawable.TextDrawable;

public final class UserInitial {

    private final String userName;

    public UserInitial(String userName) {
        this.userName = userName;
    }

    public Drawable getDrawable() {
        String initial = userName.substring(0, 1);
        return TextDrawable.builder()
                .buildRound(initial, Color.RED);
    }
}

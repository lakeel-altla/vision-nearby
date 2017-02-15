package com.lakeel.altla.vision.nearby.presentation.view.drawable;

import android.graphics.drawable.Drawable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

public final class UserInitial {

    private final String userName;

    public UserInitial(String userName) {
        this.userName = userName;
    }

    public Drawable getDrawable() {
        String initial = userName.substring(0, 1);

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(userName);

        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();

        return builder.build(initial, color);
    }
}

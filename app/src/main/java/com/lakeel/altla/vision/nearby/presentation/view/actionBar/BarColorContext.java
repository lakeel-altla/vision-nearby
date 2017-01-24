package com.lakeel.altla.vision.nearby.presentation.view.actionBar;

public final class BarColorContext {

    private final BarColor barColor;

    public BarColorContext(BarColor barColor) {
        this.barColor = barColor;
    }

    public void draw() {
        barColor.draw();
    }
}

package com.lakeel.altla.vision.nearby.presentation.view.color;

public enum AppColor {
    PRIMARY(android.graphics.Color.argb(100, 255, 193, 7));

    private int value;

    AppColor(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

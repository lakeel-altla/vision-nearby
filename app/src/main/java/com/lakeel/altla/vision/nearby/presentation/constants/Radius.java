package com.lakeel.altla.vision.nearby.presentation.constants;

public enum Radius {
    GOOGLE_MAP(50.0);

    private double value;

    Radius(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}

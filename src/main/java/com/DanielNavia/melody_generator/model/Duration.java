package com.DanielNavia.melody_generator.model;

import lombok.Getter;

@Getter
public enum Duration {
    SIXTEENTH(0.25),
    EIGHTH(0.5),
    QUARTER(1),
    HALF(2);

    private final double beats;

    Duration(double beats) {
        this.beats = beats;
    }
}
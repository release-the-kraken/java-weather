package com.sda.forecast;

import java.util.Arrays;

public enum WindDirection {
 //   N(337,360),
    N(337.6f, 22.5f),
    NE(22.6f, 67.5f),
    E(67.6f, 112.5f),
    SE(112.6f, 157.5f),
    S(157.6f, 202.5f),
    SW(202.6f, 247.5f),
    W(247.5f, 292.5f),
    NW(292.6f, 337.5f);//todo rethink degrees

    final float min_degrees;
    final float max_degrees;

    WindDirection(float min_degrees, float max_degrees) {
        this.min_degrees = min_degrees;
        this.max_degrees = max_degrees;
    }
    static WindDirection getWindDirectionSymbol(int degrees){
        return Arrays.stream(values())
                .filter(wd -> degrees >= wd.min_degrees && degrees < wd.max_degrees )
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Degrees must be between 0 and 360"));
    }
}

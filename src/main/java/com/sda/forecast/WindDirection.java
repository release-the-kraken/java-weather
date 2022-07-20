package com.sda.forecast;

import java.util.Arrays;

public enum WindDirection {
    N0(0, 45),
    NE(46, 91),
    E(91, 136),
    SE(136, 181),
    S(181, 226),
    SW(226, 271),
    W(271, 316),
    NW(316, 360);

    int min_degrees;
    int max_degrees;

    WindDirection(int min_degrees, int max_degrees) {
        this.min_degrees = min_degrees;
        this.max_degrees = max_degrees;
    }
    static WindDirection getWindDirectionSymbol(int degrees){
        return Arrays.stream(values())
                .filter(wd -> degrees >= wd.min_degrees && degrees < wd.max_degrees )
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Degrees mus be between 0 and 360"));
    }
}

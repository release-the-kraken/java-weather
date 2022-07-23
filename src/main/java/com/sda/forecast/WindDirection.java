package com.sda.forecast;

import java.util.Arrays;

public enum WindDirection {
    N(337.6f,360, 0,  22.5f),
    NE(22.6f, 45,45,67.5f),
    E(67.6f, 90,90,112.5f),
    SE(112.6f, 135, 135,157.5f),
    S(157.6f, 180, 180,202.5f),
    SW(202.6f, 225, 225, 247.5f),
    W(247.5f, 270, 270,292.5f),
    NW(292.6f, 315, 315,337.5f);

    final float minDegrees;
    final float midPoint1;
    final float midPoint2;
    final float maxDegrees;

    WindDirection(float minDegrees, float midPoint1, float midPoint2, float maxDegrees) {
        this.minDegrees = minDegrees;
        this.midPoint1 = midPoint1;
        this.midPoint2 = midPoint2;
        this.maxDegrees = maxDegrees;
    }

    static WindDirection getWindDirectionSymbol(int degrees){
        return Arrays.stream(values())
                .filter(wd -> degrees >= wd.minDegrees && degrees <= wd.midPoint1 || degrees >= wd.midPoint2 && degrees <= wd.maxDegrees )
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Degrees must be between 0 and 360"));
    }
}

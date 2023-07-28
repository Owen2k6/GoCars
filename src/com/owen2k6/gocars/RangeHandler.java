//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.owen2k6.gocars;

public class RangeHandler {
    public RangeHandler() {
    }

    public static double range(double value, double max, double min) {
        if (value > max) {
            value = max;
        } else if (value < min) {
            value = min;
        }

        return value;
    }

    public static int range(int value, int max, int min) {
        if (value > max) {
            value = max;
        } else if (value < min) {
            value = min;
        }

        return value;
    }
}

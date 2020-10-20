package com.jarihanski.asteroidsgl;

import android.content.res.Resources;
import android.util.Log;

import java.util.Random;

public class Utils {
    static final Random RNG = new Random();
    static float clamp(float val, final float min, final float max) {
        if(val < min) {
            val = min;
        } else if(val > max) {
            val = max;
        }
        return val;
    }

    static float wrap(float val, final float max) {
        if(val < (float) 0) {
            val = max;
        } else if(val > max) {
            val = (float) 0;
        }
        return val;
    }

    static float between(final float min, final float max) {
        return min + RNG.nextFloat() * (max-min);
    }

    static int between(final int min, final int max) {
        return min + RNG.nextInt() * (max-min);
    }

    public static int pxToDp(final int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(final int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static void expect(final boolean condition, final String tag) {
        Utils.expect(condition, tag, "Expectation was broken.");
    }
    public static void expect(final boolean condition, final String tag, final String message) {
        if(!condition) {
            Log.e(tag, message);
        }
    }
    public static void require(final boolean condition) {
        Utils.require(condition, "Assertion failed!");
    }
    public static void require(final boolean condition, final String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static final double TO_DEG = 180.0/Math.PI;
    public static final double TO_RAD = Math.PI/180.0;
}

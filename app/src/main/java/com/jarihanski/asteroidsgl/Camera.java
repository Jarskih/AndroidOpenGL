package com.jarihanski.asteroidsgl;

import android.content.res.Resources;
import android.opengl.Matrix;


public class Camera {
    static float METERS_TO_SHOW_X = 160f;
    static float METERS_TO_SHOW_Y = 90f;
    private float _x;
    private float _y;

    public Camera(float showX) {
        float width = Resources.getSystem().getDisplayMetrics().widthPixels;
        float height = Resources.getSystem().getDisplayMetrics().heightPixels;
        float aspectRatio = width/height;

        METERS_TO_SHOW_X = showX;
        METERS_TO_SHOW_Y = showX/aspectRatio;

        _x = METERS_TO_SHOW_X/2;
        _y = METERS_TO_SHOW_Y/2;
    };

    void lookAt(float x, float y, float[] viewportMatrix) {
        final int offset = 0;
        final float left = 0;
        final float right = METERS_TO_SHOW_X;
        final float bottom = METERS_TO_SHOW_Y;
        final float top = 0;
        final float near = 0f;
        final float far = 1f;
        Matrix.orthoM(viewportMatrix, offset, left, right, bottom, top, near, far);
        Matrix.translateM(viewportMatrix, offset, _x - x, _y - y, 0);
    }
}

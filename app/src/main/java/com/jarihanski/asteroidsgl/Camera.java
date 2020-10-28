package com.jarihanski.asteroidsgl;

import android.content.res.Resources;
import android.opengl.Matrix;


public class Camera {
    public float[] viewportMatrix = new float[4*4]; //In essence, it is our our Camera
    static float METERS_TO_SHOW_X;
    static float METERS_TO_SHOW_Y;
    private float _x;
    private float _y;
    private int _offset = 0;
    private float _left = 0;
    private float _right = 0;
    private float _bottom = 0;
    private float _top = 0;
    private float _near = 0f;
    private float _far = 0;

    public Camera(float showX) {
        float width = Resources.getSystem().getDisplayMetrics().widthPixels;
        float height = Resources.getSystem().getDisplayMetrics().heightPixels;
        float aspectRatio = width/height;

        METERS_TO_SHOW_X = showX;
        METERS_TO_SHOW_Y = showX/aspectRatio;

        _x = METERS_TO_SHOW_X/2;
        _y = METERS_TO_SHOW_Y/2;

        _offset = 0;
        _left = 0;
        _right = METERS_TO_SHOW_X;
        _bottom = METERS_TO_SHOW_Y;
        _top = 0;
        _near = 0f;
        _far = 1f;
    };

    void lookAt(float x, float y, float[] viewportMatrix) {
        Matrix.orthoM(viewportMatrix, _offset, _left, _right, _bottom, _top, _near, _far);
        float diffX = _x - x;
        float diffY = _y - y;
        diffX = Utils.clamp(diffX, -_right, 0);
        diffY = Utils.clamp(diffY, -_bottom, 0);
        Matrix.translateM(viewportMatrix, _offset, diffX, diffY, 0);
    }
}

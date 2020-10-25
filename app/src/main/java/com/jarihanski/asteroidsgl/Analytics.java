package com.jarihanski.asteroidsgl;

import android.graphics.Color;

public class Analytics {

    private float _accumulator = 0;
    private int _frames = 0;
    private Text _fpsCounter = null;
    private double _msPerFrame;

    Analytics() {
    }

    void update(double dt) {
        _accumulator += dt;
        _frames += 1;
        if(_accumulator > 1) {
            _msPerFrame = 1000.0/_frames;
            _frames = 0;
            _accumulator = 0;
        }
    }

    void render(final float[] viewportMatrix) {
        _fpsCounter = new Text(String.valueOf((int)_msPerFrame) + " mspf", 5, 5);
        _fpsCounter.setColors(Colors.yellow);
        _fpsCounter.render(viewportMatrix);
    }
}
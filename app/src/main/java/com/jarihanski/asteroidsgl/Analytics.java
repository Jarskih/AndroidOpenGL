package com.jarihanski.asteroidsgl;

import android.content.Context;
import android.opengl.Matrix;

public class Analytics {
    private float _accumulator = 0;
    private int _frames = 0;
    private Text _fpsCounter = null;
    private double _msPerFrame;

    Analytics() {
    }

    void update(final double dt) {
        _accumulator += dt;
        _frames += 1;
        if(_accumulator > 1) {
            _msPerFrame = _accumulator * 1000 / _frames;
            _frames = 0;
            _accumulator = 0;
        }
    }

    void render(final Context context, final float[] viewportMatrix, final Player player) {
        final int offset = 15;
        _fpsCounter = new Text(context.getText(R.string.mspf) + " " + (float)_msPerFrame, offset, Config.WORLD_HEIGHT - offset);
        _fpsCounter.setColors(Colors.yellow);
        _fpsCounter.render(viewportMatrix);
    }
}

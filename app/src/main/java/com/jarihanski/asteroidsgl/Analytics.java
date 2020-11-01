package com.jarihanski.asteroidsgl;

import android.content.Context;

public class Analytics {
    private float _accumulator = 0;
    private int _frames = 0;
    private Text _frameTime = null;
    private Text _fpsText = null;
    private double _fps;
    private double _msPerFrame;
    private boolean _update;
    final int _offset = 15;
    private double _speed = 10;

    Analytics(Context context) {
        _update = true;
        _frameTime = new Text("", _offset, Config.WORLD_HEIGHT - _offset);
        _fpsText = new Text("", _offset, Config.WORLD_HEIGHT - _offset*2);
    }

    void update(final double dt) {
        _accumulator += dt;
        if(_accumulator > 1) {
            _msPerFrame = _accumulator * 1000 / _frames;
            _fps = _frames/_accumulator;
            _frames = 0;
            _accumulator = 0;
            _update = true;
        }

        if(_frameTime != null) {
            _frameTime._x += dt * _speed;
            if(_frameTime._x > Config.WORLD_WIDTH) {
                _frameTime._x = 0;
            }
        }

        if(_fpsText != null) {
            _fpsText._x += dt * _speed;
            if(_fpsText._x > Config.WORLD_WIDTH) {
                _fpsText._x = 0;
            }
        }
    }

    void render(final Context context, final float[] viewportMatrix) {
        _frames += 1;

        if(_update) {
            _frameTime = new Text(context.getText(R.string.mspf) + " " + (float)_msPerFrame, _frameTime._x, Config.WORLD_HEIGHT - _offset);
            _fpsText = new Text("fps " + (float)_fps, _fpsText._x, Config.WORLD_HEIGHT - _offset*2);
            _update = false;
        }
        _frameTime.setColors(Colors.yellow);
        _frameTime.render(viewportMatrix);
        _fpsText.setColors(Colors.yellow);
        _fpsText.render(viewportMatrix);
    }
}

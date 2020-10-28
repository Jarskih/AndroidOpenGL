package com.jarihanski.asteroidsgl;

import android.content.Context;
import android.opengl.Matrix;

public class Hud {
    private Text _wave = null;
    private Text _asteroids = null;
    private float _x;
    private float _y;

    public Hud() {
    }

    public void render(final Player player, final Context context, final float[] viewportMatrix, final int wave, final int asteroids) {
        final int offset = 15;
        _x = 0;
        _y = 0;
        _wave = new Text(context.getString(R.string.wave) +" "+ wave, offset, offset);
        _asteroids = new Text(context.getString(R.string.asteroids_remaining) +" "+ asteroids, offset, offset*2);
        _wave.setColors(Colors.yellow);
        _asteroids.setColors(Colors.yellow);
        _wave.render(viewportMatrix);
        _asteroids.render(viewportMatrix);
    }
}

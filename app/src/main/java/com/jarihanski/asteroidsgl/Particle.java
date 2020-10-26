package com.jarihanski.asteroidsgl;

import android.opengl.GLES20;

public class Particle extends GLEntity {
    private static final float MIN_VEL = 8f;
    private static final float MAX_VEL = -8f;
    private static final float SIZE = 2;
    private static final float PARTICLE_SIZE = 2;
    private static final int PARTICLE_POINTS = 3;


    private int _points;
    private double _accumulator;
    private double _lifetime = 1f;

    public Particle(final float x, final float y) {
        _x = x;
        _y = y;

        _points = PARTICLE_POINTS;
        _width = SIZE;
        _velX = Utils.between(MIN_VEL, MAX_VEL);
        _velY = Utils.between(MIN_VEL, MAX_VEL);

        setColors(Colors.yellow);
        _height = _width;
        final double radius = _width/2;
        final float[] verts = Mesh.generateLinePolygon(_points, radius);
        _mesh = new Mesh(verts, GLES20.GL_LINES);
        _mesh.setWidthHeight(_width, _height);
    }

    @Override
    public void onCollision(final GLEntity that) {
    }

    public void update(final double dt) {
        super.update(dt);
        _rotation++;
        _accumulator += dt;

        if(_accumulator > _lifetime) {
            _isAlive = false;
        }
    }

    public void reset() {
        _accumulator = 0;
        _isAlive = true;
    }
}
package com.jarihanski.asteroidsgl;

import android.opengl.GLES20;

public class Asteroid extends GLEntity {
    private static final float BIG_MIN_VEL = 4;
    private static final float BIG_MAX_VEL = -4f;

    private static final float MEDIUM_MIN_VEL = 6f;
    private static final float MEDIUM_MAX_VEL = -6f;

    private static final float SMALL_MIN_VEL = 8f;
    private static final float SMALL_MAX_VEL = -8f;

    // Sizes
    private static final float BIG_SIZE = 12;
    private static final float MEDIUM_SIZE = 8;
    private static final float SMALL_SIZE = 4;

    // Shapes
    public static class Points {
        private static final int BIG = 9;
        private static final int MEDIUM = 7;
        private static final int SMALL = 5;
    }

    public static class AsteroidType {
        final static int BIG = 2;
        final static int MEDIUM = 1;
        final static int SMALL = 0;
    }

    private int _points;
    private int _asteroidSize;
    private double _accumulator;
    private double _breakDelay = 0.1f;

    public Asteroid(final float x, final float y, int size) {
        _x = x;
        _y = y;

        _asteroidSize = size;
        create(size);
    }

    private void create(int size) {
        switch (size) {
            case AsteroidType.BIG: {
                _points = Points.BIG;
                _width = BIG_SIZE;
                _velX = Utils.between(BIG_MIN_VEL, BIG_MAX_VEL);
                _velY = Utils.between(BIG_MIN_VEL, BIG_MAX_VEL);
            }
            break;
            case AsteroidType.MEDIUM: {
                _points = Points.MEDIUM;
                _width = MEDIUM_SIZE;
                _velX = Utils.between(MEDIUM_MIN_VEL, MEDIUM_MAX_VEL);
                _velY = Utils.between(MEDIUM_MIN_VEL, MEDIUM_MAX_VEL);
            }
            break;
            case AsteroidType.SMALL: {
                _points = Points.SMALL;
                _width = SMALL_SIZE;
                _velX = Utils.between(SMALL_MIN_VEL, SMALL_MAX_VEL);
                _velY = Utils.between(SMALL_MIN_VEL, SMALL_MAX_VEL);
            }
            break;
            default: {
            }
            break;
        }

        _height = _width;
        final double radius = _width/2;
        final float[] verts = Mesh.generateLinePolygon(_points, radius);
        _mesh = new Mesh(verts, GLES20.GL_LINES);
        _mesh.setWidthHeight(_width, _height);
    }

    @Override
    public void onCollision(final GLEntity that) {
        if(!canBreak()) {
            return;
        }

        _game.spawnParticles(_x, _y);
        _game.onGameEvent(Game.GameEvent.Explosion);

        _accumulator = 0;
        _asteroidSize -= 1;
        if(_asteroidSize >= 0) {
            create(_asteroidSize);
        } else {
            _isAlive = false;
        }
    }

    private boolean canBreak() {
        return _accumulator > _breakDelay;
    }

    public void update(final double dt) {
        super.update(dt);
        _rotation++;
        _accumulator += dt;
    }
}

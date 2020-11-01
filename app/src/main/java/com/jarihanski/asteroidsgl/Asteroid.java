package com.jarihanski.asteroidsgl;

import android.opengl.GLES20;

public class Asteroid extends GLEntity {

    public static class AsteroidType {
        final static int BIG = 2;
        final static int MEDIUM = 1;
        final static int SMALL = 0;
    }

    private int _points;
    private int _asteroidSize;
    private double _accumulator;

    public Asteroid(final float x, final float y, int size) {
        _x = x;
        _y = y;

        _asteroidSize = size;
        create(size);

        _shader = new Shader(_game.getContext());
        _shader.create(R.raw.vertex, R.raw.fragment);
        _format = new VertexFormat();
        _format.addAttribute(0, Mesh.COORDS_PER_VERTEX, GLES20.GL_FLOAT, NORMALIZED, Mesh.VERTEX_STRIDE, _mesh._vertexBuffer);
    }

    private void create(int size) {
        switch (size) {
            case AsteroidType.BIG: {
                _points = Config.Points.BIG;
                _width = Config.BIG_SIZE;
                _velX = Utils.between(Config.BIG_MIN_VEL, Config.BIG_MAX_VEL);
                _velY = Utils.between(Config.BIG_MIN_VEL, Config.BIG_MAX_VEL);
            }
            break;
            case AsteroidType.MEDIUM: {
                _points = Config.Points.MEDIUM;
                _width = Config.MEDIUM_SIZE;
                _velX = Utils.between(Config.MEDIUM_MIN_VEL, Config.MEDIUM_MAX_VEL);
                _velY = Utils.between(Config.MEDIUM_MIN_VEL, Config.MEDIUM_MAX_VEL);
            }
            break;
            case AsteroidType.SMALL: {
                _points = Config.Points.SMALL;
                _width = Config.SMALL_SIZE;
                _velX = Utils.between(Config.SMALL_MIN_VEL, Config.SMALL_MAX_VEL);
                _velY = Utils.between(Config.SMALL_MIN_VEL, Config.SMALL_MAX_VEL);
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
        double _breakDelay = 0.1f;
        return _accumulator > _breakDelay;
    }

    public void update(final double dt) {
        super.update(dt);
        _rotation++;
        _accumulator += dt;
    }
}

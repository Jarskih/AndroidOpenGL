package com.jarihanski.asteroidsgl;

import android.opengl.GLES20;

public class Asteroid extends GLEntity {
    private static final float MAX_VEL = 8f;
    private static final float MIN_VEL = -8f;

    public Asteroid(final float x, final float y, int points) {
        if (points < 3) {
            points = 3;
        } //triangles or more, please. :)
        _x = x;
        _y = y;
        _velX = Utils.between(MIN_VEL, MAX_VEL);
        _velY = Utils.between(MIN_VEL, MAX_VEL);
        _width = 12;
        _height = _width;
        final double radius = 6.0;
        final int numVerts = points * 2; //we render lines, and each line requires 2 points
        final float[] verts = Mesh.generateLinePolygon(points, radius);
        _mesh = new Mesh(verts, GLES20.GL_LINES);
        _mesh.setWidthHeight(_width, _height);
    }

        public void update(final double dt) {
            super.update(dt);
            _rotation++;
        }
    }

package com.jarihanski.asteroidsgl;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.util.Log;

import static com.jarihanski.asteroidsgl.Mesh.generateLinePolygon;

public class Bullet extends GLEntity {
    private final static String TAG = "Bullet";
    private static final Mesh BULLET_MESH = new Mesh(generateLinePolygon(3, 0), GLES20.GL_POINTS); //Q&D pool, Mesh.POINT is just [0,0,0] float array
    private static final float TO_RADIANS = (float) Math.PI / 180.0f;

    public float _ttl = Config.TIME_TO_LIVE;

    public Bullet() {
        setColors(251.0f / 255, 241.0f / 255, 107.0f / 255, 1.0f);
        _mesh = BULLET_MESH; //all bullets use the exact same mesh

        _shader = new Shader(_game.getContext());
        if(!_shader.create(R.raw.vertex, R.raw.fragment)) {
            Log.e(TAG, "Failed to create shader");
        }
        _format = new VertexFormat();
        _format.addAttribute(0, Mesh.COORDS_PER_VERTEX, GLES20.GL_FLOAT, NORMALIZED, Mesh.VERTEX_STRIDE, _mesh._vertexBuffer);
    }

    public void fireFrom(GLEntity source) {
        final float theta = source._rotation * TO_RADIANS;
        _x = source._x + (float) Math.sin(theta) * (source._width * 0.5f);
        _y = source._y - (float) Math.cos(theta) * (source._height * 0.5f);
        _velX = source._velX;
        _velY = source._velY;
        _velX += (float) Math.sin(theta) * Config.BULLET_SPEED;
        _velY -= (float) Math.cos(theta) * Config.BULLET_SPEED;
        _ttl = Config.TIME_TO_LIVE;
        _isAlive = true;
    }

    @Override
    public boolean isDead() {
        return _ttl < 1 || !_isAlive;
    }

    @Override
    public void update(double dt) {
        if (_ttl > 0) {
            _ttl -= dt;
            super.update(dt);
        }
    }

    @Override
    public void render(final float[] viewportMatrix){
        if(_ttl > 0) {
            super.render(viewportMatrix);
        }
    }

    @Override
    public boolean isColliding(final GLEntity that){
        if(!areBoundingSpheresOverlapping(this, that)){ //quick rejection
            return false;
        }
        final PointF[] asteroidVerts = that.getPointList();
        return CollisionDetection.polygonVsPoint(asteroidVerts, _x, _y);
    }
}
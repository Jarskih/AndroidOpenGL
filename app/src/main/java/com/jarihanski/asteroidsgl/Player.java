package com.jarihanski.asteroidsgl;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.util.Log;

public class Player extends GLEntity {
    private static final String TAG = "Player";
    private float _bulletCooldown = 0;

    public Player(float x, float y){
        super();
        _x = x;
        _y = y;
        _width = Config.PLAYER_WIDTH;
        _height = Config.PLAYER_HEIGHT;
        float[] vertices = Triangle.vertices;
        setColors(_color);
        _mesh = new Mesh(vertices, GLES20.GL_TRIANGLES);
        _mesh.setWidthHeight(_width, _height);
        _mesh.flipY();

        _shader = new Shader(_game.getContext());
        if(!_shader.create(R.raw.vertex, R.raw.fragment)) {
            Log.e(TAG, "Failed to create shader");
        }
        _format = new VertexFormat();
        _format.addAttribute(0, Mesh.COORDS_PER_VERTEX, GLES20.GL_FLOAT, NORMALIZED, Mesh.VERTEX_STRIDE, _mesh._vertexBuffer);
    }

    static final float ROTATION_VELOCITY = 360f; //TODO: game play values!
    static final float THRUST = 2f;
    static final float DRAG = 0.99f;

    @Override
    public void update(double dt){
        _rotation += (dt*ROTATION_VELOCITY) * _game._inputs._horizontalFactor;
        if(_game._inputs._pressingB){
            final float theta = (float) (_rotation*Utils.TO_RAD);
            _velX += (float)Math.sin(theta) * THRUST;
            _velY -= (float)Math.cos(theta) * THRUST;
        }
        _velX *= DRAG;
        _velY *= DRAG;

        _bulletCooldown -= dt;
        if(_game._inputs._fireButtonJustPressed && _bulletCooldown <= 0){
            setColors(1, 0, 1, 1);
            if(_game.maybeFireBullet(this)){
                _bulletCooldown = Config.TIME_BETWEEN_SHOTS;
            }
        }else{
            setColors(Colors.yellow);
        }

        super.update(dt);
    }

    @Override
    public void onCollision(GLEntity that) {
        _isAlive = false;
    }

    @Override
    public void render(float[] viewportMatrix) {
        super.render(viewportMatrix);
    }

    @Override
    public boolean isColliding(final GLEntity that){
        if(!areBoundingSpheresOverlapping(this, that)){
            return false;
        }
        final PointF[] shipHull = getPointList();
        final PointF[] asteroidHull = that.getPointList();
        if(CollisionDetection.polygonVsPolygon(shipHull, asteroidHull)){
            return true;
        }
        return CollisionDetection.polygonVsPoint(asteroidHull, _x, _y); //finally, check if we're inside the asteroid
    }

    public void respawn() {
        _x = Config.WORLD_WIDTH/2f;
        _y = Config.WORLD_HEIGHT/2f;
        _isAlive = true;
    }
}

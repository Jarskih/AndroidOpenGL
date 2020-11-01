package com.jarihanski.asteroidsgl;

import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.Matrix;

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
        _shader.create(R.raw.vertex, R.raw.fragment);
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
        final int OFFSET = 0;
        //reset the model matrix and then translate (move) it into world space
        Matrix.setIdentityM(modelMatrix, OFFSET); //reset model matrix
        Matrix.translateM(modelMatrix, OFFSET, _x, _y, _depth);
        //viewportMatrix * modelMatrix combines into the viewportModelMatrix
        //NOTE: projection matrix on the left side and the model matrix on the right side.
        Matrix.multiplyMM(viewportModelMatrix, OFFSET, viewportMatrix, OFFSET, modelMatrix, OFFSET);
        //apply a rotation around the Z-axis to our modelMatrix. Rotation is in degrees.
        Matrix.setRotateM(modelMatrix, OFFSET, _rotation, 0, 0, 1.0f);
        //apply scaling to our modelMatrix, on the x and y axis only.
        Matrix.scaleM(modelMatrix, OFFSET, _scale, _scale, 1f);
        //finally, multiply the rotated & scaled model matrix into the model-viewport matrix
        //creating the final rotationViewportModelMatrix that we pass on to OpenGL
        Matrix.multiplyMM(rotationViewportModelMatrix, OFFSET, viewportModelMatrix, OFFSET, modelMatrix, OFFSET);

        _shader.bind();
        _shader.set_uniform_vec4("color", _color);
        _shader.set_uniform_mat4("modelViewProjection", rotationViewportModelMatrix);
        _format.bind();

        assert(_mesh._drawMode == GLES20.GL_TRIANGLES
                || _mesh._drawMode == GLES20.GL_LINES
                || _mesh._drawMode == GLES20.GL_POINTS);
        // draw the previously uploaded vertices
        GLES20.glDrawArrays(_mesh._drawMode, OFFSET, _mesh._vertexCount);
        // disable vertex array
        _shader.clear("position");
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

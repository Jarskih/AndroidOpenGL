package com.jarihanski.asteroidsgl;

import android.opengl.GLES20;

public class Border extends GLEntity {
    public Border(final float x, final float y, final float worldWidth, final float worldHeight){
        super();
        _x = x;
        _y = y;
        _width = worldWidth-1.0f; //-1 so the border isn't obstructed by the screen edge
        _height = worldHeight-1.0f;
        setColors(1f, 0f, 0f, 1f); //RED for visibility
        _mesh = new Mesh(Mesh.generateLinePolygon(4, 10.0), GLES20.GL_LINES);
        _mesh.rotateZ(45* Utils.TO_RAD);
        _mesh.setWidthHeight(_width, _height); //will automatically normalize the mesh!
    }
}
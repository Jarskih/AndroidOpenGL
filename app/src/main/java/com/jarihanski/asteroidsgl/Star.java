package com.jarihanski.asteroidsgl;

import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;

public class Star extends GLEntity {
    private static Mesh m = null; //Q&D pool
    private final static String TAG = "Star";

    public Star(final float x, final float y){
        super();
        _x = x;
        _y = y;
        _color[0] = Color.red(Color.YELLOW) / 255f;
        _color[1] = Color.green(Color.YELLOW) / 255f;
        _color[2] = Color.blue(Color.YELLOW) / 255f;
        _color[3] = 1f;
        if(m == null) {
            final float[] vertices = {0, 0, 0};
            m = new Mesh(vertices, GLES20.GL_POINTS);
        }
        _mesh = m; //all Stars use the exact same Mesh instance.
        setColors(1.0f, 1.0f,1.0f, 1.0f);

        _shader = new Shader(_game.getContext());
        if(!_shader.create(R.raw.vertex, R.raw.fragment)) {
            Log.e(TAG, "Failed to create shader");
        }
        _format = new VertexFormat();
        _format.addAttribute(0, Mesh.COORDS_PER_VERTEX, GLES20.GL_FLOAT, NORMALIZED, Mesh.VERTEX_STRIDE, _mesh._vertexBuffer);
    }
}

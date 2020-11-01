package com.jarihanski.asteroidsgl;

public class Triangle extends Mesh {
    static final float[] vertices = { // in counterclockwise order:
            0.0f,  0.5f, 0.0f, 	// top
            -0.5f, -0.5f, 0.0f,	// bottom left
            0.5f, -0.5f, 0.0f  	// bottom right
    };
    public Triangle() {
        super(vertices);
    }
}

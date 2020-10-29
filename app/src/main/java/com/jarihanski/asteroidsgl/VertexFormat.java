package com.jarihanski.asteroidsgl;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;

public class VertexFormat {
    private int ATTRIBUTE_COUNT = 4;
    private int _stride;
    private int _attribute_count;
    private Attribute _attributes[];

    private class Attribute
    {
        Attribute(){
        };

        public int _index;
        public int _size;
        public int _type;
        public boolean _normalized;
        public int _stride;
        FloatBuffer _vertexBuffer;
    };

    VertexFormat() {
        _attributes = new Attribute[ATTRIBUTE_COUNT];
    }

    void addAttribute(final int index, final int size, final int type, final boolean normalized, int stride, final FloatBuffer vertexBuffer) {
        assert(_attribute_count < ATTRIBUTE_COUNT); //Trying to add more attributes than allowed

        int at = _attribute_count++;
        _attributes[at] = new Attribute();
        _attributes[at]._index = index;
        _attributes[at]._size = size;
        _attributes[at]._type = type;
        _attributes[at]._normalized = normalized;
        _attributes[at]._stride = stride;
        _attributes[at]._vertexBuffer = vertexBuffer;
    }

    boolean isValid() {
        return _attribute_count > 0;
    }

    void bind() {
        for (int index = 0; index < _attribute_count; index++)
        {
            GLES20.glDisableVertexAttribArray(index);	//We don't know the previous vertex format. So we have to disable the previous formats
        }

        for (int index = 0; index < _attribute_count; index++)
        {
			final Attribute attrib = _attributes[index];
            glEnableVertexAttribArray(index);
            glVertexAttribPointer(attrib._index, attrib._size, attrib._type, attrib._normalized, _stride, attrib._vertexBuffer);
        }
    };
}

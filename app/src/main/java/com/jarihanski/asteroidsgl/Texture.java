package com.jarihanski.asteroidsgl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Texture {

    private int[] _id;

    Texture() {

    }

    public void create(final Context context, final int resourceId)
    {
        final int[] handle = new int[1];
        GLES20.glGenTextures(1, handle, 0);

        if (handle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handle[0]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
        }

        if (handle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        _id = handle;
    }

    public boolean is_valid() {
        return _id[0] > 0;
    }

    void bind()
    {
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, _id[0]); // bind 0 to clear bind
    }

    public void destroy() {
        if (!is_valid()) {
            return;
        }

        int OFFSET = 0;
        GLES20.glDeleteTextures(1, _id, OFFSET);
        _id[0] = 0;
    }
}

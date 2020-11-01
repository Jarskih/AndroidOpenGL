package com.jarihanski.asteroidsgl;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.jarihanski.asteroidsgl.GLManager.checkGLError;

public class Shader {
    private static String TAG = "Shader";

    private static String vertexShaderCode;
    private static String fragmentShaderCode;
    private static Context _context;

    //handles to various GL objects:
    public int glProgramHandle; //handle to the compiled shader program

    Shader(Context context) {
        _context = context;
    }

    public boolean create(final int vertex_shader_id, final int fragment_shader_id)
    {
        if (is_valid())
        {
            return false;
        }

        loadShaders(vertex_shader_id, fragment_shader_id);
        final int vertex = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        final int fragment = compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        glProgramHandle = linkShaders(vertex, fragment);
        // delete the shaders as they're linked into our program now and no longer necessary
        GLES20.glDeleteShader(vertex);
        GLES20.glDeleteShader(fragment);
        //activate the program
        GLES20.glUseProgram(glProgramHandle);
        GLES20.glLineWidth(5f); //draw lines 5px wide
        checkGLError("buildProgram");
        return true;
    }

    public void bind()
    {
        GLES20.glUseProgram(glProgramHandle);
    }

    public boolean set_uniform_mat4(final String name, final float[] value) {
        final int location = get_uniform_location(name);

        if (location == -1) {
            return false;
        }

        final int COUNT = 1;
        final boolean TRANSPOSED = false;
        final int OFFSET = 0;
        GLES20.glUniformMatrix4fv(location, COUNT, TRANSPOSED, value, OFFSET);

        return true;
    }

    public boolean set_uniform_vec4(final String name, final float[] value) {
        final int location = get_uniform_location(name);

        if (location == -1) {
            return false;
        }

        final int OFFSET = 0;
        final int COUNT = 1;
        GLES20.glUniform4fv(location, COUNT, value, OFFSET);

        return true;
    }

    private int get_attrib_location(final String name) {
        bind();
        return GLES20.glGetAttribLocation(glProgramHandle, name); // Returns -1 if fails
    }

    private int get_uniform_location(final String name) {
        bind();
        return GLES20.glGetUniformLocation(glProgramHandle, name); // Returns -1 if fails
    }

    public void clear(String name) {
        final int id = get_attrib_location(name);
        if(id == -1) {
            Log.e(TAG, "Error trying to get attribute location: " + name);
        }
        GLES20.glDisableVertexAttribArray(id);
    }

    private static boolean loadShaders(int vertex_id, int fragment_id) {
        try {
            char[] buffer = new char[256];
            BufferedReader reader = new BufferedReader(new InputStreamReader(_context.getResources().openRawResource(vertex_id)));
            int bytes = reader.read(buffer);
            vertexShaderCode = new String(buffer, 0, bytes);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            char[] buffer = new char[256];
            BufferedReader reader = new BufferedReader(new InputStreamReader(_context.getResources().openRawResource(fragment_id)));
            int bytes = reader.read(buffer);
            fragmentShaderCode = new String(buffer, 0, bytes);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static int compileShader(final int type, final String shaderCode){
        assert(type == GLES20.GL_VERTEX_SHADER || type == GLES20.GL_FRAGMENT_SHADER);
        final int handle = GLES20.glCreateShader(type); // Create a shader object and store its handle
        GLES20.glShaderSource(handle, shaderCode); // Pass in the code
        GLES20.glCompileShader(handle); // then compile the shader
        Log.d(TAG, "Shader Compile Log: \n" + GLES20.glGetShaderInfoLog(handle));
        checkGLError("compileShader");
        return handle;
    }

    private static int linkShaders(final int vertexShader, final int fragmentShader){
        final int handle = GLES20.glCreateProgram();
        GLES20.glAttachShader(handle, vertexShader);
        GLES20.glAttachShader(handle, fragmentShader);
        GLES20.glLinkProgram(handle);
        Log.d(TAG, "Shader Link Log: \n" + GLES20.glGetProgramInfoLog(handle));
        checkGLError("linkShaders");
        return handle;
    }

    public void destroy()
    {
        if (!is_valid())
        {
            return;
        }

        GLES20.glDeleteProgram(glProgramHandle);
        glProgramHandle = 0;
    }

    private boolean is_valid()
    {
        return glProgramHandle != 0;
    }
}

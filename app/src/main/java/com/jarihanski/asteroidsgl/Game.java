package com.jarihanski.asteroidsgl;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Game extends GLSurfaceView implements GLSurfaceView.Renderer {
    private static final int BACKGROUND_COLOR = Color.rgb(135, 206, 235);
    private Player _player;
    private Border _border;

    static float WORLD_WIDTH = 160f; //all dimensions are in meters
    static float WORLD_HEIGHT = 90f;
    static float METERS_TO_SHOW_X = 160f; //160m x 90m, the entire game world in view
    static float METERS_TO_SHOW_Y = 90f; //TODO: calculate to match screen aspect ratio

    // Create the projection Matrix. This is used to project the scene onto a 2D viewport.
    private float[] _viewportMatrix = new float[4*4]; //In essence, it is our our Camera

    private static int STAR_COUNT = 100;
    private ArrayList<Star> _stars= new ArrayList();

    private static int ASTEROID_COUNT = 20;
    private ArrayList<Asteroid> _asteroids = new ArrayList();

    private ArrayList<Text> _texts = new ArrayList<>();

    public static long SECOND_IN_NANOSECONDS = 1000000000;
    public static long MILLISECOND_IN_NANOSECONDS = 1000000;
    public static float NANOSECONDS_TO_MILLISECONDS = 1.0f / MILLISECOND_IN_NANOSECONDS;
    public static float NANOSECONDS_TO_SECONDS = 1.0f / SECOND_IN_NANOSECONDS;

    private static final int BULLET_COUNT = (int)(Bullet.TIME_TO_LIVE/Player.TIME_BETWEEN_SHOTS)+1;
    Bullet[] _bullets = new Bullet[BULLET_COUNT];

    public InputManager _inputs = new InputManager(); //empty but valid default
    public void setControls(final InputManager input){
        _inputs = input;
    }

    public Game(Context context) {
        super(context);
        Init();
    }

    public Game(Context context, AttributeSet attributes) {
        super(context, attributes);
        Init();
    }

    private void Init() {
        GLEntity._game = this;
        setEGLContextClientVersion(2); //select OpenGL ES 2.0
        setPreserveEGLContextOnPause(true); //context *may* be preserved and thus *may* avoid slow reloads when switching apps.
        // we always re-create the OpenGL context in onSurfaceCreated, so we're safe either way.

        setRenderer(this);
    }

    //trying a fixed time-step with accumulator, courtesy of
    //   https://gafferongames.com/post/fix_your_timestep/
    final double dt = 0.01;
    double accumulator = 0.0;
    double currentTime = System.nanoTime()*NANOSECONDS_TO_SECONDS;
    private void update(){
        final double newTime = System.nanoTime()*NANOSECONDS_TO_SECONDS;
        final double frameTime = newTime - currentTime;
        currentTime = newTime;
        accumulator += frameTime;
        while(accumulator >= dt){
            for(final Asteroid a : _asteroids){
                a.update(dt);
            }

            for(final Bullet b : _bullets){
                if(b.isDead()){ continue; } //skip
                b.update(dt);
            }

            _player.update(dt);

            collisionDetection();
            removeDeadEntities();

            accumulator -= dt;
        }
    }

    private void render(){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT); //clear buffer to background color
        //setup a projection matrix by passing in the range of the game world that will be mapped by OpenGL to the screen.
        //TODO: encapsulate this in a Camera-class, let it "position" itself relative to an entity
        final int offset = 0;
        final float left = 0;
        final float right = METERS_TO_SHOW_X;
        final float bottom = METERS_TO_SHOW_Y;
        final float top = 0;
        final float near = 0f;
        final float far = 1f;
        Matrix.orthoM(_viewportMatrix, offset, left, right, bottom, top, near, far);

        _border.render(_viewportMatrix);
        for(final Asteroid a : _asteroids){
            a.render(_viewportMatrix);
        }
        for(final Star s : _stars){
            s.render(_viewportMatrix);
        }
        _player.render(_viewportMatrix);

        for(final Text t : _texts){
            t.render(_viewportMatrix);
        }

        for(final Bullet b : _bullets){
            if(b.isDead()){ continue; } //skip
            b.render(_viewportMatrix);
        }
    }

    public boolean maybeFireBullet(final GLEntity source){
        for(final Bullet b : _bullets) {
            if(b.isDead()) {
                b.fireFrom(source);
                return true;
            }
        }
        return false;
    }

    private void collisionDetection(){
        for(final Bullet b : _bullets) {
            if(b.isDead()){ continue; } //skip dead bullets
            for(final Asteroid a : _asteroids) {
                if(b.isColliding(a)){
                    if(a.isDead()){continue;}
                    b.onCollision(a); //notify each entity so they can decide what to do
                    a.onCollision(b);
                }
            }
        }
        for(final Asteroid a : _asteroids) {
            if(a.isDead()){continue;}
            if(_player.isColliding(a)){
                _player.onCollision(a);
                a.onCollision(_player);
            }
        }
    }

    public void removeDeadEntities(){
        Asteroid temp;
        final int count = _asteroids.size();
        for(int i = count-1; i >= 0; i--){
            temp = _asteroids.get(i);
            if(temp.isDead()){
                _asteroids.remove(i);
            }
        }
    }

    // OpenGL callbacks

    @Override
    public void onSurfaceCreated(final GL10 unused, final EGLConfig config) {
        float red = Color.red(BACKGROUND_COLOR) / 255f;
        float green = Color.green(BACKGROUND_COLOR) / 255f;
        float blue = Color.blue(BACKGROUND_COLOR) / 255f;
        float alpha = 1f;

        // Set clear color
        GLES20.glClearColor(red, green, blue, alpha);

        // build shader program
        GLManager.buildProgram();

        _player = new Player(WORLD_WIDTH/2f, 10);
        //Keep the Border inside the game view
        _border = new Border(WORLD_WIDTH/2, WORLD_HEIGHT/2, WORLD_WIDTH, WORLD_HEIGHT);

        Random r = new Random();
        for(int i = 0; i < STAR_COUNT; i++){
            _stars.add(new Star(r.nextInt((int)WORLD_WIDTH), r.nextInt((int)WORLD_HEIGHT)));
        }

        for(int i = 0; i < ASTEROID_COUNT; i++) {
            _asteroids.add(new Asteroid(r.nextInt((int)WORLD_WIDTH), r.nextInt((int)WORLD_HEIGHT), r.nextInt(10)));
        }

        for(int i = 0; i < BULLET_COUNT; i++){
            _bullets[i] = new Bullet();
        }
    }

    @Override
    public void onSurfaceChanged(final GL10 unused, final int width, final int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(final GL10 unused) {
        update(); //TODO: move updates away from the render thread...
        render();
    }
}

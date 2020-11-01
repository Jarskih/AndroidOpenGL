package com.jarihanski.asteroidsgl;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final String TAG = "Config";

    public static float PLAYER_WIDTH;
    public static float PLAYER_HEIGHT;
    public static float WORLD_WIDTH;
    public static float WORLD_HEIGHT;
    public static int STAR_COUNT;
    public static int ASTEROID_TYPES;
    public static int ASTEROIDS_AT_START;
    public static int PARTICLES_PER_EXPLOSION;
    public static int MAX_PARTICLES;
    public static int ASTEROID_INCREASE_PER_WAVE;
    public static float TIME_BETWEEN_SHOTS;
    public static float BULLET_SPEED;
    public static float TIME_TO_LIVE;

    public static final long SECOND_IN_NANOSECONDS = 1000000000;
    public static final long MILLISECOND_IN_NANOSECONDS = 1000000;
    public static final float NANOSECONDS_TO_MILLISECONDS = 1.0f / MILLISECOND_IN_NANOSECONDS;
    public static final float NANOSECONDS_TO_SECONDS = 1.0f / SECOND_IN_NANOSECONDS;
    public static int BULLET_COUNT;

    //Asteroids
    public static float BIG_MIN_VEL;
    public static float BIG_MAX_VEL;
    public static float MEDIUM_MIN_VEL;
    public static float MEDIUM_MAX_VEL;
    public static float SMALL_MIN_VEL;
    public static float SMALL_MAX_VEL;

    // Sizes
    public static float BIG_SIZE;
    public static float MEDIUM_SIZE;
    public static float SMALL_SIZE;

    // Shapes
    public static class Points {
        public static int BIG;
        public static int MEDIUM;
        public static int SMALL;
    }

    public static float TEXT_SCALE;

    Config(final Context context) {
        PLAYER_WIDTH = readConfigFloat(context, "PLAYER_WIDTH");
        PLAYER_HEIGHT = readConfigFloat(context, "PLAYER_HEIGHT");
        WORLD_WIDTH = readConfigFloat(context, "WORLD_WIDTH");
        WORLD_HEIGHT = readConfigFloat(context, "WORLD_HEIGHT");
        STAR_COUNT = readConfigInt(context, "STAR_COUNT");
        ASTEROID_TYPES = readConfigInt(context, "ASTEROID_TYPES");
        ASTEROIDS_AT_START = readConfigInt(context, "ASTEROIDS_AT_START");
        PARTICLES_PER_EXPLOSION = readConfigInt(context, "PARTICLES_PER_EXPLOSION");
        MAX_PARTICLES = readConfigInt(context, "MAX_PARTICLES");
        ASTEROID_INCREASE_PER_WAVE = readConfigInt(context, "ASTEROID_INCREASE_PER_WAVE");
        TIME_BETWEEN_SHOTS = readConfigFloat(context, "TIME_BETWEEN_SHOTS");
        BULLET_SPEED = readConfigFloat(context, "BULLET_SPEED");
        TIME_TO_LIVE = readConfigFloat(context, "TIME_TO_LIVE");

        BIG_MIN_VEL = readConfigFloat(context, "BIG_MIN_VEL");
        BIG_MAX_VEL = readConfigFloat(context, "BIG_MAX_VEL");
        MEDIUM_MIN_VEL = readConfigFloat(context, "MEDIUM_MIN_VEL");
        MEDIUM_MAX_VEL = readConfigFloat(context, "MEDIUM_MAX_VEL");
        SMALL_MIN_VEL = readConfigFloat(context, "SMALL_MIN_VEL");
        SMALL_MAX_VEL = readConfigFloat(context, "SMALL_MAX_VEL");
        BIG_SIZE = readConfigFloat(context, "BIG_SIZE");
        MEDIUM_SIZE = readConfigFloat(context, "MEDIUM_SIZE");
        SMALL_SIZE = readConfigFloat(context, "SMALL_SIZE");
        Points.BIG = readConfigInt(context, "POINTS_BIG");
        Points.MEDIUM = readConfigInt(context, "POINTS_MEDIUM");
        Points.SMALL = readConfigInt(context, "POINTS_SMALL");

        TEXT_SCALE = readConfigFloat(context, "TEXT_SCALE");

        BULLET_COUNT = (int)(TIME_TO_LIVE/TIME_BETWEEN_SHOTS)+1;
    }

    // https://stackoverflow.com/questions/5140539/android-config-file
    private int readConfigInt(final Context context, final String name) {
        Resources resources = context.getResources();

        try {
            InputStream rawResource = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);
            return Integer.parseInt(properties.getProperty(name));
        } catch (IOException e) {
            Log.e(TAG, "Cant open a file");
        }

        return 0;
    }

    private float readConfigFloat(Context context, String name) {
        Resources resources = context.getResources();

        try {
            InputStream rawResource = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);
            return Float.parseFloat(properties.getProperty(name));
        } catch (IOException e) {
            Log.e(TAG, "Cant open a file");
        }

        return 0;
    }
}

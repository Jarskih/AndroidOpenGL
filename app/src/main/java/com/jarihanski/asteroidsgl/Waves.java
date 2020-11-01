package com.jarihanski.asteroidsgl;

import java.util.ArrayList;
import java.util.Random;

public class Waves {
    private int _currentWave;
    private int _asteroids;
    final ArrayList<Asteroid> _wave;
    final Random _r;

    Waves() {
        _r = new Random();
        _wave = new ArrayList<Asteroid>();
    }

    public ArrayList<Asteroid> start() {
        clean();
        _currentWave = 1;
        _asteroids = Config.ASTEROIDS_AT_START;
        return createAsteroids();
    }

    public ArrayList<Asteroid> nextWave() {
        clean();
        _currentWave += 1;
        _asteroids += Config.ASTEROID_INCREASE_PER_WAVE;
        return createAsteroids();
    }

    private ArrayList<Asteroid> createAsteroids() {
        for(int i = 0; i < _asteroids; i++) {
            _wave.add(new Asteroid(_r.nextInt((int)Config.WORLD_WIDTH), _r.nextInt((int) Config.WORLD_HEIGHT), _r.nextInt(Config.ASTEROID_TYPES)));
        }
        return _wave;
    }

    public void clean() {
        _wave.clear();
    }

    public int getWave() {
        return _currentWave;
    }
}

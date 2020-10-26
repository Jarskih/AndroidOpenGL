package com.jarihanski.asteroidsgl;

import java.util.ArrayList;

public class ParticleSystem {
    private ArrayList<Particle> _particles = null;
    private int _index = 0;

    ParticleSystem(int maxParticles) {
        _particles = new ArrayList<Particle>();
        for(int i = 0; i < maxParticles; i++) {
            _particles.add(new Particle(0,0));
        }
    }

    public Particle getParticle(float x, float y) {
        if(_index >= _particles.size()) {
            _index = 0;
        }
        Particle p = _particles.get(_index);
        _index += 1;
        p._x = x;
        p._y = y;
        p.reset();
        return p;
    }
}

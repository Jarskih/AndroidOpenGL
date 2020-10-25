package com.jarihanski.asteroidsgl;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

public class SoundPlayer {
    private static final String TAG = "Sound player";
    private static final float DEFAULT_SFX_VOLUME = 1;
    private SoundPool _soundPool;
    private static final int MAX_STREAMS = 3;
    private final Context _context;
    private HashMap<Game.GameEvent, Integer>_soundsMap;

    public SoundPlayer(Context context) {
        AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        _soundPool = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(MAX_STREAMS).build();
        _context = context;
        loadSounds();
    }

    private void loadSounds(){
        createSoundPool();
        _soundsMap = new HashMap();
        loadEventSound(Game.GameEvent.Explosion, "sounds/explosion.wav");
        loadEventSound(Game.GameEvent.Shoot, "sounds/laser.wav");
        //loadEventSound(Game.GameEvent.PlayerDied, "sounds/lose.wav");
    }

    private void loadEventSound(final Game.GameEvent event, final String fileName){
        try {
            AssetFileDescriptor afd = _context.getAssets().openFd(fileName);
            int soundId = _soundPool.load(afd, 1);
            _soundsMap.put(event, soundId);
        }catch(IOException e){
            Log.e(TAG, "loadEventSound: error loading sound " + e.toString());
        }
    }

    @SuppressWarnings("deprecation")
    private void createSoundPool() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            _soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }
        else {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            _soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(MAX_STREAMS)
                    .build();
        }
    }

    private void unloadSounds(){
        if(_soundPool != null) {
            _soundPool.release();
            _soundPool = null;
            _soundsMap.clear();
        }
    }

    public void playSoundForGameEvent(Game.GameEvent event){
        final int priority = 1;
        final int loop = 0; //-1 loop forever, 0 play once
        final float rate = 1.0f;
        final Integer soundID = (Integer) _soundsMap.get(event);
        if(soundID != null){
            _soundPool.play(soundID, DEFAULT_SFX_VOLUME, DEFAULT_SFX_VOLUME, priority, loop, rate);
        }
    }

    public void destroy() {
        unloadSounds();
    }
}

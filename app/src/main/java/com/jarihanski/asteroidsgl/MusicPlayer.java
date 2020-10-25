package com.jarihanski.asteroidsgl;

import android.content.Context;
import android.media.MediaPlayer;

import static android.media.MediaPlayer.create;

public class MusicPlayer {
    private MediaPlayer _mediaPlayer = null;
    private final Context _context;

    public MusicPlayer(Context context) {
        _context = context;
    }

    public void pause() {
        if(_mediaPlayer != null && _mediaPlayer.isPlaying()) {
            _mediaPlayer.pause();
        }
    }

    public void destroy() {
        if(_mediaPlayer != null) {
            _mediaPlayer.stop();
            _mediaPlayer.reset();
            _mediaPlayer.release();
        }
        _mediaPlayer = null;
    }

    public void playMusic() {
        destroy();
        _mediaPlayer = create(_context, R.raw.music);

        _mediaPlayer.setLooping(true);

        _mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                _mediaPlayer.start();
            }
        });

        _mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
            }
        });
    }
}

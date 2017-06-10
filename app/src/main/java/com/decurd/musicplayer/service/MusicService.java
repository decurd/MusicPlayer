package com.decurd.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

/**
 * Created by decurd on 2017-06-10.
 */

public class MusicService extends Service {

    public static final String ACTION_PLAY = "play";
    public static final String ACTION_PAUSE = "pause";

    private MediaPlayer mMediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        EventBus.getDefault().register(this);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case ACTION_PLAY:
                playMusic((Uri)intent.getParcelableExtra("uri"));
                break;
            case ACTION_PAUSE:
                break;
        }
        return START_STICKY;
    }

    @Subscribe
    public void playMusic(Uri uri) {
        try {
            mMediaPlayer.setDataSource(this, uri);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    /**
                     * {@link com.decurd.musicplayer.fragments.MusicContrallerFragment#updatePlayButton(Boolean)}
                     */
                    EventBus.getDefault().post(isPlaying());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void clickPlayButton(View v) {
        if (isPlaying()) {
            mMediaPlayer.pause();
        } else {
            mMediaPlayer.start();
        }

        /**
         * {@link com.decurd.musicplayer.fragments.MusicContrallerFragment#updatePlayButton(Boolean)}
         */
        EventBus.getDefault().post(isPlaying());
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }
}

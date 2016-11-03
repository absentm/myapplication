package com.example.dm.myapplication.find;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * FindMusicPlayService
 * Created by dm on 16-11-3.
 */
public class FindMusicPlayService extends Service {
    private String musicUrl;
    private String musicTitle;
    private String musicArtist;
    private long musicAlbum_id;

    private MediaPlayer mMediaPlayer = new MediaPlayer();
    public final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        FindMusicPlayService getService() {
            return FindMusicPlayService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        musicUrl = intent.getStringExtra("url");
        musicTitle = intent.getStringExtra("title");
        musicArtist = intent.getStringExtra("artist");
        musicAlbum_id = intent.getLongExtra("album_id", 0);

        try {
            mMediaPlayer.setDataSource(musicUrl);
            mMediaPlayer.prepare();

            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            } else {
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }
}

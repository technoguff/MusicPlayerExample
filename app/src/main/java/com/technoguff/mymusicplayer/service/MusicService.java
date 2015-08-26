package com.technoguff.mymusicplayer.service;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import com.technoguff.mymusicplayer.model.MusicFile;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{


    private MediaPlayer mMusicPlayer;
    private ArrayList<MusicFile> mMusicList;

    private MyMusicBinder musicServiceBinder =  new MyMusicBinder();

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return  musicServiceBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mMusicPlayer = new MediaPlayer();
        mMusicPlayer.setOnCompletionListener(this);
        mMusicPlayer.setOnErrorListener(this);
        mMusicPlayer.setOnPreparedListener(this);
    }


    @Override
    public boolean onUnbind(Intent intent) {

        mMusicPlayer.stop();
        mMusicPlayer.release();

        return super.onUnbind(intent);
    }

    public void setMusicList(ArrayList<MusicFile> musicFiles){
        mMusicList = musicFiles;
    }

    public void playMusic(int position){
        mMusicPlayer.reset();
        MusicFile music = mMusicList.get(position);
        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, music.getId());
        try {
            mMusicPlayer.setDataSource(MusicService.this, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMusicPlayer.prepareAsync();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.e("MusicService", "Completed");
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.e("MusicService", "Error");
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }



    public class MyMusicBinder extends Binder {
        public MusicService getService(){
            return MusicService.this;
        }
    }
}

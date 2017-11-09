package com.example.emmalady.musicplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.emmalady.musicplayer.R;
import com.example.emmalady.musicplayer.activity.MainActivity;
import com.example.emmalady.musicplayer.model.Song;
import com.example.emmalady.musicplayer.utils.Constants;

import java.util.ArrayList;


/**
 * Created by Liz Nguyen on 08/11/2017.
 */

public class MusicServices extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{

    private final String LOG_TAG = "MusicService";
    private String songTitle = "";
    private String songArtist = "";
    private static final int NOTIFY_ID = 1;
    private Notification status;

    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPosn;

    private final IBinder musicBind = new MusicBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize position
        songPosn = 0;
        //create player
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Song> songList){
        this.songs = songList;
    }

    public class MusicBinder extends Binder {
        public MusicServices getService() {
            return MusicServices.this;
        }
    }

    public void playSong(){
        player.reset();
        //get song
        Song playSong = songs.get(songPosn);
        //get id
        long currSong = playSong.getmID();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong);
        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e(Constants.NOTIFICATION.MUSIC_SERVICE, Constants.NOTIFICATION.ERROR_DATA, e);
        }
        player.prepareAsync();
    }
    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        pushNotification();
    }
    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void setSong(int songIndex){
        songPosn = songIndex;
    }

    public void pausePlayer(){
        player.pause();
    }

    public void go(){
        player.start();
    }

    public void pushNotification(){
        Song playSong = songs.get(songPosn);
        songTitle = playSong.getmTitle();
        songArtist = playSong.getmArtist();
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_push);
        contentView.setImageViewResource(R.id.ibPlay, R.drawable.ic_play_arrow_blue_24dp);
        contentView.setTextViewText(R.id.title, songTitle);
        contentView.setTextViewText(R.id.text, songArtist);

        Intent playIntent = new Intent(this, MusicServices.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getBroadcast(this, 100, playIntent, 0);

        contentView.setOnClickPendingIntent(R.id.ibPlay, pplayIntent);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentIntent(pendInt)
                .setSmallIcon(R.drawable.apollo_holo_dark_play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContent(contentView);
        Notification notification = mBuilder.build();

        startForeground(NOTIFY_ID, notification);
    }
}

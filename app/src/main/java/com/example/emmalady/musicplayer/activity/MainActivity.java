package com.example.emmalady.musicplayer.activity;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.example.emmalady.musicplayer.R;
import com.example.emmalady.musicplayer.adapter.SongAdapter;
import com.example.emmalady.musicplayer.model.Song;
import com.example.emmalady.musicplayer.model.controller.MusicController;
import com.example.emmalady.musicplayer.service.MusicServices;
import com.example.emmalady.musicplayer.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private ActionBar mActionBar;
    public static ArrayList<Song> mSongList;
    private ListView mListViewMusic;
    private SongAdapter songAdapter;
    private Song song;
    public static MediaPlayer mp;

    public static MusicServices musicSrv;
    public static Intent playIntent;
    public static boolean musicBound = false;

    public static boolean paused = false;
    MusicController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mListViewMusic = (ListView) findViewById(R.id.lvListMusic);
        mSongList = new ArrayList<>();
        getSongList();

        //Sort ABC
        Collections.sort(mSongList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getmTitle().compareTo(b.getmTitle());
            }
        });
        songAdapter = new SongAdapter(this, mSongList);
        mListViewMusic.setAdapter(songAdapter);
        mListViewMusic.setClickable(true);
        mListViewMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, PlayMusicActivity.class);
                song = mSongList.get(position);
                musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
                musicSrv.playSong();

                String title = song.getmTitle();
                String artist = song.getmArtist();
                i.putExtra(Constants.KEY.KEY_TITLE, title);
                i.putExtra(Constants.KEY.KEY_ARTIST, artist);
                startActivity(i);
                //startService();
            }
        });
        customActionBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicServices.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(Constants.ACTION.PLAY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //Get Columns
            int columnID = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int columnTitle = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int columnArtist = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);

            //Add Songs to songList
            do {
                long id = musicCursor.getLong(columnID);
                String title = musicCursor.getString(columnTitle);
                String artist = musicCursor.getString(columnArtist);
                mSongList.add(new Song(id, title, artist));
            } while (musicCursor.moveToNext());
        }
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicServices.MusicBinder binder = (MusicServices.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(mSongList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void customActionBar() {
        this.mActionBar = getSupportActionBar();
        this.mActionBar.setDisplayOptions(16);
        this.mActionBar.setCustomView((int) R.layout.custom_actionbar);
        this.mActionBar.setDisplayShowHomeEnabled(true);
        this.mActionBar.setTitle(getString(R.string.app_name));
    }

    public void startService(View v) {
        Intent serviceIntent = new Intent(MainActivity.this, MusicServices.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_push);
            Notification notification = new Notification(R.drawable.apollo_holo_dark_play, null, System.currentTimeMillis());

            String action = intent.getAction();

            if (action.equalsIgnoreCase(Constants.ACTION.PLAY_ACTION)) {
                if (paused) {
                    paused = false;
                    musicSrv.pausePlayer();
                    remoteViews.setImageViewResource(R.id.ibPlay, R.drawable.apollo_holo_dark_play);
                } else {
                    musicSrv.go();
                    paused = true;
                    remoteViews.setImageViewResource(R.id.ibPlay, R.drawable.apollo_holo_dark_pause);
                }
            }
        }
    };
}

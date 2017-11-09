package com.example.emmalady.musicplayer.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;

import com.example.emmalady.musicplayer.R;
import com.example.emmalady.musicplayer.model.controller.MusicController;
import com.example.emmalady.musicplayer.service.MusicServices;
import com.example.emmalady.musicplayer.utils.Constants;

public class PlayMusicActivity extends MainActivity{
    private ActionBar mActionBar;
    private TextView tvTitle;
    private TextView tvArtist;
    private TextView tvID;
    private ImageButton ibBack;
    private ImageButton ibPlay;
    private MusicController controller;
    private MediaPlayer mp;
//    /boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewByID();
        Intent getIntent = getIntent();
        String title = getIntent.getStringExtra(Constants.KEY.KEY_TITLE);
        String artist = getIntent.getStringExtra(Constants.KEY.KEY_ARTIST);
        tvTitle.setText(title);
        tvArtist.setText(artist);
        //paused = true;
        customActionBar();
    }

    public void findViewByID() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvArtist = (TextView) findViewById(R.id.tvArtist);
        ibPlay = (ImageButton) findViewById(R.id.ibPlay);
        ibPlay.setImageResource(R.drawable.apollo_holo_dark_pause);
    }

    private void customActionBar() {
        this.mActionBar = getSupportActionBar();
        this.mActionBar.setDisplayOptions(16);
        this.mActionBar.setCustomView((int) R.layout.custom_back_actionbar);
        this.mActionBar.setDisplayShowHomeEnabled(true);
        this.mActionBar.setTitle(getString(R.string.app_name));
        View view = getSupportActionBar().getCustomView();

        ibBack = (ImageButton) view.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusicActivity.this.finish();
            }
        });
    }
    public void playSong(View v){
        if(paused){
            paused = false;
            musicSrv.pausePlayer();
            ibPlay.setImageResource(R.drawable.apollo_holo_dark_play);
        } else {
            musicSrv.go();
            paused = true;
            ibPlay.setImageResource(R.drawable.apollo_holo_dark_pause);

        }
    }

}

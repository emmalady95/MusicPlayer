package com.example.emmalady.musicplayer.model;

import static android.R.attr.id;

/**
 * Created by Liz Nguyen on 08/11/2017.
 */

public class Song {
    private long mID;
    private String mTitle;
    private String mArtist;


    public Song(final long id, final String title, final String artist) {
        this.mID = id;
        this.mTitle = title;
        this.mArtist = artist;
    }

    public long getmID() {
        return mID;
    }

    public void setmID(long mID) {
        this.mID = mID;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmArtist() {
        return mArtist;
    }

    public void setmArtist(String mArtist) {
        this.mArtist = mArtist;
    }
}

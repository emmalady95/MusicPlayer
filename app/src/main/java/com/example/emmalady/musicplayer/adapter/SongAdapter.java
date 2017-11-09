package com.example.emmalady.musicplayer.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.emmalady.musicplayer.R;
import com.example.emmalady.musicplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liz Nguyen on 08/11/2017.
 */

public class SongAdapter extends BaseAdapter {
    //private Context mContext;
    private ArrayList<Song> songList;
    private LayoutInflater songInflater;//songs

    public SongAdapter(Context context, ArrayList<Song> songList){
        this.songInflater=LayoutInflater.from(context);
        this.songList = songList;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Map to item_song layout
        LinearLayout llItemSong = (LinearLayout)songInflater.inflate(R.layout.item_music, parent, false);

        //Get title and artist views
        TextView songTitle = (TextView)llItemSong.findViewById(R.id.tvSongTitle);
        TextView songArtist = (TextView)llItemSong.findViewById(R.id.tvSongArtist);

        //songList = new ArrayList<>();
        //Get song using position
        Song curr = songList.get(position);

        //get title and artist strings
        songTitle.setText(curr.getmTitle());
        songArtist.setText(curr.getmArtist());
        //set position as tag
        llItemSong.setTag(position);
        return llItemSong;
    }

}

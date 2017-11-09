package com.example.emmalady.musicplayer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.emmalady.musicplayer.R;


/**
 * Created by Liz Nguyen on 09/11/2017.
 */

public class Constants {
    public interface KEY{
        public static final String KEY_ID = "ID";
        public static final String KEY_TITLE = "TITLE";
        public static final String KEY_ARTIST = "ARTIST";
    }
    public interface NOTIFICATION{
        public static final String MUSIC_SERVICE = "MUSIC SERVICE";
        public static final String ERROR_DATA = "Error setting data source";

    }
    public interface ACTION {
        public static String PLAY_ACTION = "com.example.emmalady.musicplayer.action.play";
        public static String STARTFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.startforeground";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }
}

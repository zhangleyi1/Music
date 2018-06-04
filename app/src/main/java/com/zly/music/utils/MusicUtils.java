package com.zly.music.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.zly.music.bean.MusicData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/22.
 */
public class MusicUtils {
    private static String TAG = "MusicUtils";
    private static final Map<String, MusicData> musicHash = new HashMap<>();

    public static ArrayList<MusicData> getMusicData(Context context) {
        ArrayList<MusicData> list = new ArrayList<MusicData>();
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MusicData music = new MusicData();
                music.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                music.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                music.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                music.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                music.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                music.setFileSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                music.setFileName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                music.setAlbumId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
                music.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                String albumArt = getAlbumArt(context, (int) music.getAlbumId());
                Bitmap bm = null;
                if (albumArt != null) {
                    bm = BitmapFactory.decodeFile(albumArt);
                    music.setBitmap(bm);
                }

                if (music.getFileSize() > 1000 * 800) {
                    Log.d("zly", "zly --> music: " + music.toString());
                    list.add(music);
                }
            }
        }
        cursor.close();

        return list;
    }

    private static String getAlbumArt(Context context, int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[]{"album_art"};
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)), projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }

        cur.close();
        cur = null;
        return album_art;
    }

    public static String getMusicPath(int position) {
        /*MusicData music = musicHash.containsKey(id) ? musicHash.get(id) : null;
        musicHash.
        if (null != music) {
            return music.getPath();
        }
        return null;*/
        Iterator it = musicHash.keySet().iterator();

        int i = 0;
        for (Map.Entry<String, MusicData> entry : musicHash.entrySet()) {
            Log.d(TAG, "zly --> getMusicPath i:" + i + " position:" + position);
            if (i == position) {
//                Map.Entry entry = (Map.Entry)it.next();
                return ((MusicData)entry.getValue()).getPath();
            }
//            entry.getKey();
//            entry.getValue();
            i++;
        }
/*

        for (int i = 0; it.hasNext(); it.next()) {
            Log.d(TAG, "zly --> i:" + i);
            if (i == position) {
                Map.Entry entry = (Map.Entry)it.next();
                return ((MusicData)entry.getValue()).getPath();
            }
        }
*/
        Log.d(TAG, "zly --> getMusicPath no find current song.");
        return null;
    }

    public static void setMusic(String id, MusicData music) {
        if (!musicHash.containsKey(id)) {
            musicHash.put(id, music);
        }
//        Log.d(TAG, "zly --> setMusic.");
//        getMusicPath(0);
    }

    public static MusicData getMusic(String id) {
        if (musicHash.containsKey(id)) {
            return musicHash.get(id);
        }
        return null;
    }

    public static void removeMusic(String id) {
        if (musicHash.containsKey(id)) {
            musicHash.remove(id);
        }
    }

    public static MusicData getAddMusicData(Context context, long id) {
        Cursor sCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media._ID + "='" + id + "'", null, null);
        Log.d(TAG, "zly --> add.size:" + sCursor.getCount() + " id:" + id);
        MusicData music = new MusicData();
        if (null != sCursor) {
            if (sCursor.moveToNext()) {
                music.setTitle(sCursor.getString(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                music.setArtist(sCursor.getString(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                music.setPath(sCursor.getString(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                music.setDuration(sCursor.getInt(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                music.setFileSize(sCursor.getLong(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                music.setFileName(sCursor.getString(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                music.setAlbumId(sCursor.getLong(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
                music.setAlbum(sCursor.getString(sCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                String albumArt = getAlbumArt(context, (int) music.getAlbumId());
                Bitmap bm = null;
                if (albumArt != null) {
                    bm = BitmapFactory.decodeFile(albumArt);
                    music.setBitmap(bm);
                }
            }
        }
        sCursor.close();

        if (music.getFileSize() > 1000 * 800) {
            Log.d("zly", "zly --> music: " + music.toString());
            return music;
        }

        return null;
    }

}

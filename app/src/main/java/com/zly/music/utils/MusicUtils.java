package com.zly.music.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.zly.music.bean.MusicData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/22.
 */
public class MusicUtils {

    public static ArrayList<MusicData> getMusicData(Context context) {
        ArrayList<MusicData> list = new ArrayList<MusicData>();
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MusicData music = new MusicData();
                music.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                music.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                music.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                music.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                music.setFileSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                /*
                if (music.size > 1000 * 800) {
                    // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                    if (music.song.contains("-")) {
                        String[] str = music.song.split("-");
                        music.singer = str[0];
                        music.song = str[1];
                    }
                    list.add(song);
                }
            */
                list.add(music);
                music.toString();
                // 释放资源

            }
        }
        cursor.close();

        return list;
    }

}

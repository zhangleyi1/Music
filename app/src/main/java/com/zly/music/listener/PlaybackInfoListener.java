package com.zly.music.listener;

import android.support.v4.media.session.PlaybackStateCompat;

/**
 * Created by Administrator on 2018/5/29.
 */
public abstract class PlaybackInfoListener {

    public abstract void onPlaybackStateChange(PlaybackStateCompat state);

    public void onPlaybackCompleted() {
    }

}

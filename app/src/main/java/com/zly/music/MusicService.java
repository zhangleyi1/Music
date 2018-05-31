package com.zly.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.zly.music.bean.MusicData;
import com.zly.music.listener.PlaybackInfoListener;

import java.util.List;

/**
 * Created by Administrator on 2018/5/24.
 */
public class MusicService extends MediaBrowserServiceCompat {
    private static String TAG = "MusicService";

    private MediaSessionCompat mMediaSessionCompat;
    private MediaPlayerManager mMediaPlayerManager;
    private MediaNotificationManager mMediaNotificationManager;

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return new BrowserRoot("root", null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.sendResult(null);
    }

    private MediaSessionCompat.Callback mMediaSessionCallback = new MediaSessionCompat.Callback() {

        @Override
        public void onPlay() {
            super.onPlay();
            Log.d(TAG, "zly --> onPlay");

            mMediaSessionCompat.setActive(true);
            setMediaPlaybackState(PlaybackStateCompat.STATE_PLAYING);
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
        }

        @Override
        public void onPause() {
            super.onPause();
            mMediaPlayerManager.onPause();
            setMediaPlaybackState(PlaybackStateCompat.STATE_PAUSED);
        }

        @Override
        public void onStop() {
            super.onStop();
            mMediaSessionCompat.setActive(false);
            setMediaPlaybackState(PlaybackStateCompat.STATE_STOPPED);

        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            super.onPlayFromMediaId(mediaId, extras);
            Log.d(TAG, "zly --> onPlayFromMediaId begin");
        }


    };

   /* @Override
    public void onCommand(String command, Bundle extras, ResultReceiver cb) {
        super.onCommand(command, extras, cb);
        if( COMMAND_EXAMPLE.equalsIgnoreCase(command) ) {
            //Custom command here
        }
    }

    @Override
    public void onSeekTo(long pos) {
        super.onSeekTo(pos);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if( mMediaPlayer != null ) {
            mMediaPlayer.release();
        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaSessionCompat.release();
    }

    private void setMediaPlaybackState(int state) {
        PlaybackStateCompat.Builder playbackstateBuilder = new PlaybackStateCompat.Builder();
        if( state == PlaybackStateCompat.STATE_PLAYING ) {
            playbackstateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_PAUSE);
        } else {
            playbackstateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_PLAY);
        }
        playbackstateBuilder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0);
        mMediaSessionCompat.setPlaybackState(playbackstateBuilder.build());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "zly --> onCreate begin.");
        initMediaSession();
//        initNoisyReceiver();

        mMediaPlayerManager = new MediaPlayerManager(MusicService.this, new MediaPlayerListener());
        mMediaPlayerManager.initMediaPlayer();

        mMediaNotificationManager = MediaNotificationManager.getInstance();

    }

    private void initMediaSession() {
//        ComponentName mediaButtonReceiver = new ComponentName(getApplicationContext(), MediaButtonReceiver.class);
        mMediaSessionCompat = new MediaSessionCompat(this, "MusicService");
        mMediaSessionCompat.setCallback(mMediaSessionCallback);
        mMediaSessionCompat.setFlags( MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS |
                MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS);

        setSessionToken(mMediaSessionCompat.getSessionToken());
    }

//    private void initNoisyReceiver() {
//        //Handles headphones coming unplugged. cannot be done through a manifest receiver
//        IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
//        registerReceiver(mNoisyReceiver, filter);
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int type = intent.getIntExtra("type", -1);
        Log.d(TAG, "zly --> onStartCommand type:" + type);
        if (0 == type) {
            int musicId = intent.getLongExtra("data");
            mMediaPlayerManager.addMusicToList(musicId);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private class MediaPlayerListener extends PlaybackInfoListener {

        @Override
        public void onPlaybackStateChange(PlaybackStateCompat state) {

        }
    }

    private void reloadQueue() {

    }
}

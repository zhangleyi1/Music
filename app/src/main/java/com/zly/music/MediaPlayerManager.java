package com.zly.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.zly.music.bean.MusicData;
import com.zly.music.listener.PlaybackInfoListener;
import com.zly.music.utils.MusicUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Administrator on 2018/5/29.
 */

public class MediaPlayerManager implements AudioManager.OnAudioFocusChangeListener {

    // 上下文对象
    private Context mContext;
    // 音频播放器MediaPlayer
    private MediaPlayer mMediaPlayer;
    // 播放信息回调
    private PlaybackInfoListener mPlaybackInfoListener;
    private List<Long> mPlayList = new LinkedList<Long>();
    private static MediaPlayerManager mMediaPlayerManager;
    private static String TAG = "MediaPlayerManager";

    /**
     *
     */
    // 当前音频信息
    private MediaMetadataCompat mCurrentMedia;
    // 当前音频id
    private String mFilename;
    // 当前的播放状态
    @PlaybackStateCompat.State
    private int mState;
    // 是否播放完成
    private boolean mCurrentMediaPlayedToCompletion;

    // Work-around for a MediaPlayer bug related to the behavior of MediaPlayer.seekTo()
    // while not playing.
    private int mSeekWhileNotPlaying = -1;
    private int mCurrentPlayingPosition = 0;


    /**
     * 构造方法
     *
     * @param context
     */
    public MediaPlayerManager(Context context) {
        // 上下文对象
        mContext = context.getApplicationContext();
//        initializeMediaPlayer();
        // 播放信息回调
    }

    public static MediaPlayerManager getInstance(Context context) {
        if (null == mMediaPlayerManager) {
            mMediaPlayerManager = new MediaPlayerManager(context);
        }
        return mMediaPlayerManager;
    }

    public void registerListener(PlaybackInfoListener listener) {
        mPlaybackInfoListener = listener;
    }

    // Implements PlaybackControl.
    public void playFromMedia(MediaMetadataCompat metadata) {
        // 当前音频信息
        mCurrentMedia = metadata;
        // 音频id
        final String mediaId = metadata.getDescription().getMediaId();

        //playFile(MusicLibrary.getMusicFilename(mediaId));
        playFile("/storage/0000-12D9/韩红 - 梨花开.mp3");
    }

    public MediaMetadataCompat getCurrentMedia() {
        return mCurrentMedia;
    }

    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    protected void onPlay() {
        Log.d(TAG, "zly --> onPlay mMediaPlayer:" + (mMediaPlayer != null) + " isPlaying:" +  mMediaPlayer.isPlaying());
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            try {
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();

            }
            mMediaPlayer.start();
            setNewState(PlaybackStateCompat.STATE_PLAYING);
        }
    }

    protected void onPause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            setNewState(PlaybackStateCompat.STATE_PAUSED);
        }
    }

    public void onStop() {
        // Regardless of whether or not the MediaPlayer has been created / started, the state must
        // be updated, so that MediaNotificationManager can take down the notification.
        setNewState(PlaybackStateCompat.STATE_STOPPED);
        release();
    }

    public void seekTo(long position) {
        if (mMediaPlayer != null) {
            // 音频未播放
            if (!mMediaPlayer.isPlaying()) {
                mSeekWhileNotPlaying = (int) position;
            }
            // seek to
            mMediaPlayer.seekTo((int) position);

            // Set the state (to the current state) because the position changed and should
            // be reported to clients.
            setNewState(mState);
        }
    }

    public void setVolume(float volume) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setVolume(volume, volume);
        }
    }

    /**
     * Once the {@link MediaPlayer} is released, it can't be used again, and another one has to be
     * created. In the onStop() method of the {@link MainActivity} the {@link MediaPlayer} is
     * released. Then in the onStart() of the {@link MainActivity} a new {@link MediaPlayer}
     * object has to be created. That's why this method is private, and called by load(int) and
     * not the constructor.
     * <p>
     * 初始化mediaPlayer
     */
    private void initializeMediaPlayer() {
        // 创建MediaPlayer
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            // 音频播放完成的回调
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    // 回调音频播放完成
                    mPlaybackInfoListener.onPlaybackCompleted();

                    // Set the state to "paused" because it most closely matches the state
                    // in MediaPlayer with regards to available state transitions compared
                    // to "stop".
                    // Paused allows: seekTo(), start(), pause(), stop()
                    // Stop allows: stop()
                    setNewState(PlaybackStateCompat.STATE_PAUSED);
                }
            });
        }
    }

    public void addMusicToList(long id) {
        Long sId = Long.valueOf(id);
        mPlayList.add(sId);
        Log.d(TAG, "zly --> addMusicToList.");
        MusicData music = MusicUtils.getAddMusicData(mContext, id);
        if (null != music) {
            MusicUtils.setMusic(String.valueOf(id), music);
        }
    }

    public void removeMusicToList(long id) {
        for (int i = 0; i < mPlayList.size(); i++) {
            if (mPlayList.get(i).equals(id)) {
                mPlayList.remove(i);
                break;
            }
        }
        Log.d(TAG, "zly --> removeMusicToList.");
        MusicData music = MusicUtils.getAddMusicData(mContext, id);
        MusicUtils.removeMusic(String.valueOf(id));
    }

    /**
     * 根据音频id进行音频播放
     *
     * @param filename
     */
    public void playFile(String filename) {
        // 音频是否发生变化
        boolean mediaChanged = (mFilename == null || !filename.equals(mFilename));
        // 音频是否播放完成
        if (mCurrentMediaPlayedToCompletion) {
            // Last audio file was played to completion, the resourceId hasn't changed, but the
            // player was released, so force a reload of the media file for playback.
            mediaChanged = true;
            mCurrentMediaPlayedToCompletion = false;
        }
        // 音频未发生变化
        if (!mediaChanged) {
            // 没有播放则播放
            if (!isPlaying()) {
                play();
            }
            return;
        }
        // 音频已发生变化
        else {
            release();
        }
        // 变化后的音频id
        mFilename = filename;
        // 创建MediaPlayer
        initializeMediaPlayer();
        // 设置要播放的音频文件
        try {
            AssetFileDescriptor assetFileDescriptor = mContext.getAssets().openFd(mFilename);
            mMediaPlayer.setDataSource(
                    assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
        } catch (Exception e) {
            throw new RuntimeException("Failed to open file: " + mFilename, e);
        }
        // 准备播放
        try {
            mMediaPlayer.prepare();
        } catch (Exception e) {
            throw new RuntimeException("Failed to open file: " + mFilename, e);
        }
        // 播放
        play();
    }

    public void play() {
//        if (mAudioFocusHelper.requestAudioFocus()) {
//            registerAudioNoisyReceiver();
        try {
            String path = MusicUtils.getMusicPath(mCurrentPlayingPosition);
            Log.d(TAG, "zly --> path:" + path);
            mMediaPlayer.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "zly --> play() set dataSource failed.");
        }

        onPlay();
//        }
    }

    /**
     * 播放状态
     *
     * @param newPlayerState
     */
    // This is the main reducer for the player state machine.
    private void setNewState(@PlaybackStateCompat.State int newPlayerState) {
        // 设置播放状态
        mState = newPlayerState;

        /**
         * 状态为STOPPED，则为完成状态
         */
        // Whether playback goes to completion, or whether it is stopped, the
        // mCurrentMediaPlayedToCompletion is set to true.
        if (mState == PlaybackStateCompat.STATE_STOPPED) {
            mCurrentMediaPlayedToCompletion = true;
        }

        // Work around for MediaPlayer.getCurrentPosition() when it changes while not playing.
        final long reportPosition;
        if (mSeekWhileNotPlaying >= 0) {
            reportPosition = mSeekWhileNotPlaying;
            //
            if (mState == PlaybackStateCompat.STATE_PLAYING) {
                mSeekWhileNotPlaying = -1;
            }
        } else {
            reportPosition = mMediaPlayer == null ? 0 : mMediaPlayer.getCurrentPosition();
        }
        // 回调播放状态
        final PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder();
        stateBuilder.setActions(getAvailableActions());
        stateBuilder.setState(mState,
                reportPosition,
                1.0f,
                SystemClock.elapsedRealtime());
        // 播放状态回调
        mPlaybackInfoListener.onPlaybackStateChange(stateBuilder.build());
    }

    /**
     * Set the current capabilities available on this session. Note: If a capability is not
     * listed in the bitmask of capabilities then the MediaSession will not handle it. For
     * example, if you don't want ACTION_STOP to be handled by the MediaSession, then don't
     * included it in the bitmask that's returned.
     */
    @PlaybackStateCompat.Actions
    private long getAvailableActions() {
        long actions = PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                | PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
                | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;
        switch (mState) {
            case PlaybackStateCompat.STATE_STOPPED:
                actions |= PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PAUSE;
                break;
            case PlaybackStateCompat.STATE_PLAYING:
                actions |= PlaybackStateCompat.ACTION_STOP
                        | PlaybackStateCompat.ACTION_PAUSE
                        | PlaybackStateCompat.ACTION_SEEK_TO;
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                actions |= PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_STOP;
                break;
            default:
                actions |= PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PLAY_PAUSE
                        | PlaybackStateCompat.ACTION_STOP
                        | PlaybackStateCompat.ACTION_PAUSE;
        }
        return actions;
    }


    /**
     * 释放 MediaPlayer
     */
    private void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mMediaPlayer.pause();
            }
        });
    }

    private BroadcastReceiver mNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if( mMediaPlayer != null && mMediaPlayer.isPlaying() ) {
                mMediaPlayer.pause();
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean successfullyRetrievedAudioFocus() {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        AudioAttributes mAudioAttributes =
                new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build();
        AudioFocusRequest mAudioFocusRequest =
                new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .setAudioAttributes(mAudioAttributes)
                        .setAcceptsDelayedFocusGain(true)
                        .setOnAudioFocusChangeListener(MediaPlayerManager.this) // Need to implement listener
                        .build();

        int result = audioManager.requestAudioFocus(mAudioFocusRequest);

        return result == AudioManager.AUDIOFOCUS_GAIN;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void releaseFocus() {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        AudioFocusRequest mAudioFocusRequest =
                new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .setAudioAttributes(new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build())
                        .setAcceptsDelayedFocusGain(true)
                        .setOnAudioFocusChangeListener(MediaPlayerManager.this) // Need to implement listener
                        .build();
        audioManager.abandonAudioFocusRequest(mAudioFocusRequest);
        NotificationManagerCompat.from(mContext).cancel(1);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch( focusChange ) {
            case AudioManager.AUDIOFOCUS_LOSS: {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                break;
            }
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                mMediaPlayer.pause();
                break;
            }
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: {
                if (mMediaPlayer != null) {
                    mMediaPlayer.setVolume(0.3f, 0.3f);
                }
                break;
            }
            case AudioManager.AUDIOFOCUS_GAIN: {
                if (mMediaPlayer != null) {
                    if (!mMediaPlayer.isPlaying()) {
                        mMediaPlayer.start();
                    }
                    mMediaPlayer.setVolume(1.0f, 1.0f);
                }
                break;
            }
        }
    }
}

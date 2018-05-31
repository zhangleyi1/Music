package com.zly.music;

/**
 * Created by Administrator on 2018/5/30.
 */

public class MediaNotificationManager {

    private static MediaNotificationManager mMediaNotificationManager;

    public static MediaNotificationManager getInstance() {
        synchronized (MediaNotificationManager.class) {
            if (null == mMediaNotificationManager) {
                mMediaNotificationManager = new MediaNotificationManager();
            }
        }
        return mMediaNotificationManager;
    }


    private void showPlayingNotification() {
        /*NotificationCompat.Builder builder = MediaStyleHelper.from(this, mMediaSessionCompat);
        if( builder == null ) {
            return;
        }

        builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)));
        builder.setStyle(new NotificationCompat.MediaStyle().setShowActionsInCompactView(0).setMediaSession(mMediaSessionCompat.getSessionToken()));
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        NotificationManagerCompat.from(this).notify(1, builder.build());*/
    }

    private void showPausedNotification() {
        /*NotificationCompat.Builder builder = MediaStyleHelper.from(this, mMediaSessionCompat);
        if( builder == null ) {
            return;
        }

        builder.addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)));
        builder.setStyle(new NotificationCompat.MediaStyle().setShowActionsInCompactView(0).setMediaSession(mMediaSessionCompat.getSessionToken()));
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        NotificationManagerCompat.from(this).notify(1, builder.build());*/
    }

}

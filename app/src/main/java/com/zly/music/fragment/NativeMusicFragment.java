package com.zly.music.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zly.music.MusicService;
import com.zly.music.R;
import com.zly.music.adapter.NativeMusicAdapter;
import com.zly.music.bean.MusicData;
import com.zly.music.utils.MusicUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/5/21.
 */
public class NativeMusicFragment extends Fragment implements NativeMusicAdapter.OnItemClickListener {
    private String TAG = "NativeMusicFragment";
    private ArrayList<MusicData> mListMusic;
    private long[] mPlayMusic;
    public RecyclerView mCy;
    private NativeMusicAdapter mAdapter;
    private MediaBrowserCompat mMediaBrowser;
    private int mCurrentState;
    private MediaBrowserCompat mMediaBrowserCompat;
    private MediaControllerCompat mMediaControllerCompat;
    private static final int STATE_PAUSED = 0;
    private static final int STATE_PLAYING = 1;
    private Activity mActivity;
    private Context mContext;
    public static int TYPE_ADD_MUSIC = 100;
    public static int TYPE_DELETE_MUSIC = 101;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            MediaControllerCompat.getMediaController(mActivity).getTransportControls().play();
        }
    };

    public NativeMusicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.native_music_fragment, container, false);

        mActivity = getActivity();
        mContext = getContext();

        initView(view);
        intiData();

        return view;
    }

    private void intiData() {
        mMediaBrowserCompat = new MediaBrowserCompat(mContext, new ComponentName(mContext, MusicService.class),
                mMediaBrowserCompatConnectionCallback, null);
        mMediaBrowserCompat.connect();
    }

    private void initView(View view) {
        mCy = view.findViewById(R.id.cy);
        mListMusic = MusicUtils.getMusicData(mContext); //Get the all native Music
        mAdapter = new NativeMusicAdapter(mContext, (ArrayList<MusicData>) mListMusic);
        mAdapter.setItemClickListener(this);
        Log.d(TAG, "zly --> NativeMusicFragment.initView list.size:" + mListMusic.size());
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mCy.setLayoutManager(layoutManager);
        mCy.setItemAnimator( new DefaultItemAnimator());
        mCy.setAdapter(mAdapter);
    }

    @Override
    public void OnItemClick(int position) {
        Intent intent = new Intent(mContext, MusicService.class);
/*//超过1M字节导致程序报错，实际传值要小于512KB；
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", mListMusic);
        intent.putExtras(bundle);
*/
        intent.putExtra("type", TYPE_ADD_MUSIC);
        intent.putExtra("data", mListMusic.get(position).getId());
        Log.d(TAG, "zly --> send id:" + mListMusic.get(position).getId());
        mHandler.sendEmptyMessageDelayed(1, 1000);
        mContext.startService(intent);
    }

    private MediaControllerCompat.Callback mMediaControllerCompatCallback = new MediaControllerCompat.Callback() {

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            if( state == null ) {
                return;
            }

            switch( state.getState() ) {
                case PlaybackStateCompat.STATE_PLAYING: {
                    mCurrentState = STATE_PLAYING;
                    break;
                }
                case PlaybackStateCompat.STATE_PAUSED: {
                    mCurrentState = STATE_PAUSED;
                    break;
                }
            }
        }
    };

    private MediaBrowserCompat.ConnectionCallback mMediaBrowserCompatConnectionCallback = new MediaBrowserCompat.ConnectionCallback() {

        @Override
        public void onConnected() {
            super.onConnected();
            try {
                mMediaControllerCompat = new MediaControllerCompat(mActivity, mMediaBrowserCompat.getSessionToken());
                mMediaControllerCompat.registerCallback(mMediaControllerCompatCallback);
                //setSupportMediaController(mMediaControllerCompat);
                MediaControllerCompat.setMediaController(mActivity, mMediaControllerCompat);
                Log.d(TAG, "zly --> mMediaBrowserCompatConnectionCallback onConnected mp3:" + String.valueOf(R.raw.warner_tautz_off_broadway));
                //MediaControllerCompat.getMediaController(mActivity).getTransportControls().playFromMediaId("/storage/0000-12D9/李雪燕 - 呼伦贝尔的冬天.mp3", null);
                MediaControllerCompat.getMediaController(mActivity).getTransportControls().playFromMediaId(String.valueOf(R.raw.warner_tautz_off_broadway), null);

            } catch( RemoteException e ) {

            }
        }
    };
}

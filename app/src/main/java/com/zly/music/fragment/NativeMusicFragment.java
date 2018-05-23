package com.zly.music.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zly.music.MainActivity;
import com.zly.music.R;
import com.zly.music.adapter.NativeMusicAdapter;
import com.zly.music.bean.MusicData;
import com.zly.music.utils.MusicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/21.
 */
public class NativeMusicFragment extends Fragment {
    private ArrayList<MusicData> mListMusic;
    public RecyclerView mCy;
    private NativeMusicAdapter mAdapter;
    private String TAG = "NativeMusicFragment";

    public NativeMusicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.native_music_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mCy = view.findViewById(R.id.cy);
        mListMusic = MusicUtils.getMusicData(getContext()); //Get the all native Music
        mAdapter = new NativeMusicAdapter(getContext(), (ArrayList<MusicData>) mListMusic);
        Log.d(TAG, "zly --> NativeMusicFragment.initView list.size:" + mListMusic.size());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mCy.setLayoutManager(layoutManager);
        mCy.setItemAnimator( new DefaultItemAnimator());
        mCy.setAdapter(mAdapter);
    }
}

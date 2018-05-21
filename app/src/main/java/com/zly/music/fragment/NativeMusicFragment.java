package com.zly.music.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zly.music.R;

/**
 * Created by Administrator on 2018/5/21.
 */
public class NativeMusicFragment extends Fragment {
    public NativeMusicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout. native_music_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
    }
}

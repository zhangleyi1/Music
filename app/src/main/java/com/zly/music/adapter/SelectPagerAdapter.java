package com.zly.music.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/5/21.
 */
public class SelectPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mListFragment;

    public SelectPagerAdapter(FragmentManager fm, List<Fragment> listFragment) {
        super(fm);
        mListFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragment.get(position);
    }

    @Override
    public int getCount() {
        return mListFragment.size();
    }
}

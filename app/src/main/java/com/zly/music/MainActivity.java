package com.zly.music;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.zly.music.adapter.SelectPagerAdapter;
import com.zly.music.fragment.NativeMusicFragment;
import com.zly.music.fragment.OnlieMusicFragment;
import com.zly.music.utils.PermissionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigationview)
    public NavigationView mNavigationView;
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.drawerlayout)
    public DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle toggle;
    @BindView(R.id.tab_layout)
    public TabLayout mTabLayout;
    @BindView(R.id.pager)
    public ViewPager mViewPager;

    private List<Fragment> mListFragment = new ArrayList<Fragment>();
    private SelectPagerAdapter mAdapter;
    private PermissionManager mPermissionManager;
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind( this ) ;

        initViews();
        hideScrollBar();
        setActionBar();
        setDrawerToggle();
       // setListener();

        getPermission();
    }

    private void getPermission () {
        mPermissionManager = PermissionManager.getInstance(MainActivity.this);
        if (!mPermissionManager.checkPermission(MainActivity.this)) {
            Log.d(TAG, "zly --> begin requestPermission.");
            mPermissionManager.requestPermission(MainActivity.this);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionManager.REQUEST_PERMISSION_ALL) {
            Log.d(TAG, "zly --> onRequestPermissionsResult success.");
        }
    }

    private void initViews() {
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.pager_native_music));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.pager_online_music));

        mListFragment.add(new NativeMusicFragment());
        mListFragment.add(new OnlieMusicFragment());
        mAdapter = new SelectPagerAdapter(getSupportFragmentManager(), mListFragment);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /*去掉navigation中的滑动条*/
    private void hideScrollBar() {
        mNavigationView.getChildAt(0).setVerticalScrollBarEnabled(false);
    }

    /*设置ActionBar*/
    private void setActionBar() {
        setSupportActionBar(mToolbar);
        /*显示Home图标*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*设置Drawerlayout的开关,并且和Home图标联动*/
    private void setDrawerToggle() {
        toggle = new ActionBarDrawerToggle(this, mDrawerlayout, mToolbar, 0, 0);
        mDrawerlayout.addDrawerListener(toggle);
        /*同步drawerlayout的状态*/
        toggle.syncState();
    }

    /*设置监听器*/
    private void setListener() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerlayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

}

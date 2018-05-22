package com.zly.music.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by Administrator on 2018/5/22.
 */

public class PermissionManager {

    private static String TAG = "PermissionManager";
    private static PermissionManager mPM;
    public static int REQUEST_PERMISSION_ALL = 1;

    static final String[] PERMISSION_ALL = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static PermissionManager getInstance(Context context) {
        synchronized (PermissionManager.class) {
            if (null == mPM) {
                mPM = new PermissionManager();
            }
        }
        return mPM;
    }

    public void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, PERMISSION_ALL, REQUEST_PERMISSION_ALL);
    }

    public boolean checkPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, String.valueOf(PERMISSION_ALL)) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        Log.d(TAG, "zly --> checkPermission error.");
        return false;
    }

}

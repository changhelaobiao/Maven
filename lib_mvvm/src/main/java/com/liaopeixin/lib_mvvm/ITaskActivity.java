package com.liaopeixin.lib_mvvm;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

/**
 * 任务栈activity
 */
public abstract class ITaskActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (BaseApplication.getInstance() != null) {
                BaseApplication.getInstance().addActivityIntoTask(this);
            }
        } finally {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("ITaskAndroid", getLocalClassName() + " onResume");

        //android O fix bug orientation
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            try {
//                In android Oreo (API 26) you can not change orientation for Activity that have below line in style
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (BaseApplication.getInstance() != null) {
                BaseApplication.getInstance().removeActivityFromTask(this);
            }
        } finally {

        }
        Log.v("TITAndroid", getLocalClassName() + " goodbye");
    }

    @Override
    public void finish() {
        try {
            if (BaseApplication.getInstance() != null) {
                BaseApplication.getInstance().removeActivityFromTask(this);
            }
        } finally {

        }
        super.finish();
    }
}

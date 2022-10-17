package com.liaopeixin.lib_mvvm;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDexApplication;


/**
 * 带Activity 栈管理
 * <H3>BaseApplication</H3>
 */
public abstract class BaseApplication extends MultiDexApplication {

    protected static BaseApplication mInstance;

    private List<Activity> activityList;

    /**
     * 应用状态监听器
     */
    public interface OnAppStateChangeListener {

        /**
         * 切换到后台
         */
        void onBackgrounded();

        /**
         * 切换到前台
         */
        void onForegrounded();
    }

    private List<OnAppStateChangeListener> appStateChangeListeners;

    public static BaseApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        activityList = new ArrayList<>();
        appStateChangeListeners = new ArrayList<>();
        registerLifecycle();
        initApp();
    }

    public abstract void initApp();

    /**
     * @param activity activity堆栈
     */
    public void addActivityIntoTask(Activity activity) {
        if (activity != null) {
            activityList.add(activity);
        }
    }

    /**
     * 移除activity堆栈
     */
    public void removeActivityFromTask(Activity act) {
        try {
            if (activityList.contains(act)) {
                activityList.remove(act);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    /**
     * 关闭是所有activity
     */
    public void finishAllActivities() {

        if (activityList == null || activityList.size() == 0) {
            Log.w(getClass().getName(), "finishAllActivityies -> no activity in activityList!");
            return;
        }
        for (int i = activityList.size() - 1; i >= 0; i--) {

            activityList.get(i).finish();

        }
    }

    //judge if app in background
    // need permission GET_TASKS
    public boolean isApplicationBroughtToBackground() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(this.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    public <T extends Activity> T getActivity(Class<T> cls) {

        try {
            for (int i = activityList.size() - 1; i >= 0; i--) {

                Activity activity = activityList.get(i);
                if (activity.getClass().equals(cls)) {
                    return (T) activity;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Finish to Activity
     *
     * @param cls Activity Class
     */
    public void finishToActivity(Class cls) {

        try {
            for (int i = activityList.size() - 1; i >= 0; i--) {
                Activity activity = activityList.get(activityList.size() - 1);
                if (activity.getClass().equals(cls)) {
                    return;
                }
                activity.finish();
                activityList.remove(activity);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* 关闭到指定activity，包含指定activity */
    public void finishContainActivity(Class cls) {

        try {
            for (int i = activityList.size() - 1; i >= 0; i--) {

                Activity activity = activityList.get(i);
                if (activity.getClass().equals(cls)) {
                    activity.finish();
                    return;
                }
                activity.finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 关掉堆栈最高位置的制定页面
     **/
    public void finishParticularActivity(Class cls) {
        try {
            for (int i = activityList.size() - 1; i >= 0; i--) {
                Activity activity = activityList.get(i);
                if (activity.getClass().equals(cls)) {
                    activity.finish();
                    return;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getActivityNumberInStack(Class cls) {
        int result = 0;
        try {
            for (int i = activityList.size() - 1; i >= 0; i--) {
                Activity activity = activityList.get(i);
                if (activity.getClass().equals(cls)) {
                    result++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public void FinishActivity(Class cls) {

        try {
            for (int i = activityList.size() - 1; i >= 0; i--) {

                Activity activity = activityList.get(i);
                if (activity.getClass().equals(cls)) {
                    activity.finish();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getSecondTopActivityName() {
        if (activityList.size() > 1) {
            return activityList.get(activityList.size() - 2).getClass().getName();
        } else return "";
    }

    public String getSecondTopActivitySimpleName() {
        if (activityList.size() > 1) {
            return activityList.get(activityList.size() - 2).getClass().getSimpleName();
        } else return "";
    }

    public String getActivityListSize() {
        if (activityList != null)
            return activityList.size() + "";
        else return "";
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public Activity getTheTopActivity() {

        if (activityList.size() > 0) {
            return activityList.get(activityList.size() - 1);
        }
        return null;
    }

    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     * 添加app状态变化监听器
     *
     * @param listener 监听器实例
     */
    public void addAppStateChangeListener(OnAppStateChangeListener listener) {
        if (listener == null)
            return;
        if (appStateChangeListeners.contains(listener))
            return;
        appStateChangeListeners.add(listener);
    }

    /**
     * 移除app状态监听器
     *
     * @param listener 监听器实例
     */
    public void removeAppStateChangeListener(OnAppStateChangeListener listener) {
        appStateChangeListeners.remove(listener);
    }

    /**
     * 清空app状态监听器列表
     */
    public void clearAppStateChangeListener() {
        appStateChangeListeners.clear();
    }

    /**
     * 注册页面生命周期监听
     */
    private void registerLifecycle() {

        final Set<Activity> lifecycleActivities = new HashSet<>();

        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

                if (lifecycleActivities.size() == 0) {
                    List<OnAppStateChangeListener> listeners = new ArrayList<>(appStateChangeListeners);
                    for (OnAppStateChangeListener listener : listeners) {
                        listener.onForegrounded();
                    }
                }
                lifecycleActivities.add(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

                lifecycleActivities.remove(activity);

                if (lifecycleActivities.size() == 0) {
                    List<OnAppStateChangeListener> listeners = new ArrayList<>(appStateChangeListeners);
                    for (OnAppStateChangeListener listener : listeners) {
                        listener.onBackgrounded();
                    }
                }

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


    /**
     * 重启application
     */
    public void reStartApp(long delayMillis) {
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.i(BaseApplication.class.getName(), "app while restart at " + delayMillis + "millisecend later!");

                String packageName = getPackageName();
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getInstance().finishAllActivities();//清除activity 栈
                getInstance().startActivity(launchIntent);//调用应用luancher
                System.exit(0);
            }
        }, delayMillis);
    }
}

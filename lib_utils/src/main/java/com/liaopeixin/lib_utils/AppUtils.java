package com.liaopeixin.lib_utils;


import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

import androidx.core.app.ActivityCompat;

/**
 * @author huanghuaqiao
 * @date 2021/9/30
 * @description app配置的一些相关信息
 */
public class AppUtils {

    private static final String TAG = "AppUtils";
    public static final int NETWORK_NONE = 0; // 没有网络连接
    public static final int NETWORK_WIFI = 1; // wifi连接
    public static final int NETWORK_2G = 2; // 2G
    public static final int NETWORK_3G = 3; // 3G
    public static final int NETWORK_4G = 4; // 4G
    public static final int NETWORK_5G = 5; // 5G
    public static final int NETWORK_MOBILE = 6; // 手机流量
    public static final int NETWORK_5G_TYPE = 20; // 5G类型
    public static final int HAS_ROOT_TYPE = 1; // 已root
    public static final int NO_ROOT_TYPE = 0; // 未root
    public static int mGitBuildCode;
    /**
     * 正式包 标志位
     */
    private static Boolean isProductFlavor = null;
    private static Boolean isDEBUG = null;

    /**
     * 同步gradle build 产物的 flavor风味包值 和 build type 到全局工具类
     * 是否是product flavor  反射app 应用层的 {@link } 自定义字段：PROD  并赋值
     *
     * @param context
     */
    public static void syncGradleBuildFlavorAndBuildTypeStatus(Context context) {
        if (isProductFlavor == null) {
            try {
                String packageName = context.getPackageName();
                Class buildConfig = Class.forName(packageName + ".BuildConfig");
//                Field DEBUG = buildConfig.getField("DEBUG");
                Field PROD = buildConfig.getField("PROD");//
                PROD.setAccessible(true);
                isProductFlavor = PROD.getBoolean(null);

                Field DEBUG = buildConfig.getField("DEBUG");//
                DEBUG.setAccessible(true);
                isDEBUG = DEBUG.getBoolean(null);

            } catch (Throwable t) {
                // Do nothing
            }
        }

        if (isDEBUG == null) {
            try {
                String packageName = context.getPackageName();
                Class buildConfig = Class.forName(packageName + ".BuildConfig");
                Field DEBUG = buildConfig.getField("DEBUG");
                DEBUG.setAccessible(true);
                isDEBUG = DEBUG.getBoolean(null);
            } catch (Throwable t) {
                // Do nothing
            }
        }

    }


    public static String getAppName(int pID) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pID + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取应用名称
     *
     * @param context
     * @return
     */
    public static String getApplicationName(Application context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        if (packageManager != null) {
            String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
            return applicationName;
        } else {
            return null;
        }

    }

    /**
     * 获取应用VersionName
     */
    public static String getVersionName(Application context) {
        PackageInfo packInfo = null;
        try {
            packInfo = context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packInfo != null) {
            return packInfo.versionName;
        } else {
            return "";
        }
    }

    /**
     * 获取终端类型
     *
     * @return
     */
    public static String getPlatform() {
        return "Android";
    }

    /**
     * 获取设备id
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
            String id = telephonyManager.getDeviceId();
            if (TextUtils.isEmpty(id)) {
                return Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            } else {
                return id;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getDeviceName() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取安卓的api版本
     *
     * @return
     */
    public static int getApiVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取当前的网络类型
     *
     * @return 0：无网络  1：wifi  2:2g  3:3g  4:4g
     */
    public static int getNetType(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
        if (null == connManager) { // 为空则认为无网络
            return NETWORK_NONE;
        }
        // 获取网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORK_NONE;
        }
        // 判断是否为WIFI
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORK_WIFI;
                }
            }
        }
        // 若不是WIFI，则去判断是2G、3G、4G网
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return NETWORK_NONE;
        }
        int networkType = telephonyManager.getNetworkType();
        switch (networkType) {
            // 2G网络
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_2G;
            // 3G网络
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_3G;
            // 4G网络
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_4G;
            case NETWORK_5G_TYPE:
                return NETWORK_5G;
            default:
                return NETWORK_MOBILE;
        }

    }

    /**
     * 获取安卓系统版本号
     */
    public static String getOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机是否root
     *
     * @return 0：未root  1：已root
     */
    public static int getRootType() {
        int rootType = NO_ROOT_TYPE;

        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                rootType = NO_ROOT_TYPE;
            } else {
                rootType = HAS_ROOT_TYPE;
            }
        } catch (Exception e) {
            Log.d(TAG, "getRootType fail");
        }
        return rootType;
    }

    /**
     * 设置app的构建编号
     *
     * @param buildNo
     */
    public static void setBuildNo(int buildNo) {
        mGitBuildCode = buildNo;
    }

    /**
     * app的构建编号
     *
     * @return
     */
    public static int getBuildNo() {
        return mGitBuildCode;
    }

    /**
     * 是否是生产包（正式环境）：  pro &&  is Release BuildType
     *
     * @return
     */
    public static boolean isProdcutFlavor() {
        return isProductFlavor == null ? false : isProductFlavor.booleanValue();
    }

    public static Boolean getIsProductFlavor() {
        return isProductFlavor;
    }

    public static Boolean getIsDEBUG() {
        return isDEBUG;
    }

    /**
     * 获取version code
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int code = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }


}

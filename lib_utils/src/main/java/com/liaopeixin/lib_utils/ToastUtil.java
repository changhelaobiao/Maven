package com.liaopeixin.lib_utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 文件名 ToastUtil
 * 创建人 lpb
 * 创建日期 2018/4/17
 */
public class ToastUtil {

    private static Toast mToast;

    private static synchronized Toast newToast(Context context, String text, int duration) {
        return Toast.makeText(getApplicationContext(context), text, duration);
    }

    private static synchronized Toast newToast(Context context, int textResId, int duration) {
        return Toast.makeText(getApplicationContext(context), textResId, duration);
    }

    private static synchronized Toast newToast(Context context, CharSequence text, int duration) {
        return Toast.makeText(getApplicationContext(context), text, duration);
    }

    private static Context getApplicationContext(Context context) {
        Context target = null;
        if (context instanceof Activity) {
            target = context.getApplicationContext();
        }
        return target != null ? target : context;
    }

    private static void show(Toast toast) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = toast;
        synchronized (ToastUtil.class) {
            try {
                if (mToast == null) {
                    return;
                }
                mToast.show();
            } catch (Exception ex) {
                Log.d("ToastUtil", "toast show error" + ex.getMessage());
            }
        }
    }

    public static void showToast(Context context, String text) {
        Toast toast = newToast(context, text, Toast.LENGTH_SHORT);
        show(toast);
    }

    public static void showToast(Context context, int textResId) {
        Toast toast = newToast(context, textResId, Toast.LENGTH_SHORT);
        show(toast);
    }

    public static void showLongToast(Context context, String text) {
        Toast toast = newToast(context, text, Toast.LENGTH_LONG);
        show(toast);
    }

    public static void showLongToast(Context context, int res) {
        Toast toast = newToast(context, res, Toast.LENGTH_LONG);
        show(toast);
    }

    public static void showLongToast(Context context, CharSequence text) {
        Toast toast = newToast(context, text, Toast.LENGTH_LONG);
        show(toast);
    }

    public static void showShortToast(Context context, CharSequence text) {
        Toast toast = newToast(context, text, Toast.LENGTH_SHORT);
        show(toast);
    }

    public static void showShortToast(Context context, int resId) {
        Toast toast = newToast(context, resId, Toast.LENGTH_SHORT);
        show(toast);
    }
}

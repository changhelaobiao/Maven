package com.liaopeixin.lib_utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * author : toby
 * e-mail : 16620129640@163.com
 * time : 2022/9/26
 * desc : Toast工具类
 */
public class ToastUtils {

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
        synchronized (ToastUtils.class) {
            try {
                if (mToast == null) {
                    return;
                }
                mToast.show();
            } catch (Exception ex) {
                Log.e("ToastUtil", "toast show error" + ex.getMessage());
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

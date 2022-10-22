package com.liaopeixin.lib_utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import androidx.annotation.ColorRes;

/**
 * author : toby
 * time : 2020/8/22
 * desc : 常用公共方法
 */
public class CommonUtils {

    /**
     * 获取颜色色值
     *
     * @param context
     * @param resColor
     * @return
     */
    public static int GetResourceColor(Context context, @ColorRes int resColor) {
        Resources resources = context.getResources();
        int reColor = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            reColor = resources.getColor(resColor, context.getTheme());
        } else {
            reColor = resources.getColor(resColor);
        }

        return reColor;
    }

    /**
     * 用于切换软键盘显示/隐藏
     *
     * @param context
     */
    public static void ShowHideSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 弹出键盘
     *
     * @param editText
     */
    public static void showSoftInput(final View editText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 1000);
    }

    /**
     * 隐藏键盘
     */
    public static void hideSoftInput(final EditText editText) {
        InputMethodManager imm = (InputMethodManager) editText
                .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.showSoftInput(editText,InputMethodManager.SHOW_FORCED);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 关闭软键盘
     */
    public static void closeSoftKeyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && ((Activity) context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 将url格式参数转成map
     *
     * @param paramStr
     * @return
     */
    public static Map<String, String> UrlParamToMap(String paramStr) {
//        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, String> paramMap = new TreeMap<>();// 默认升序排序

        String[] paramArray = paramStr.split("&");
        for (String item : paramArray) {
            if (!IsStringNull(item)) {
                if (item.contains("=")) {
                    String[] valueArray = item.split("=");
                    String keyName = valueArray[0];
                    String value = "";
                    if (valueArray.length == 2) {
                        value = valueArray[1];
                    }

                    paramMap.put(keyName, value);
                }
            }
        }

        return paramMap;
    }

    /**
     * 将url格式参数转成map
     *
     * @param paramStr
     * @return
     */
    public static Map<String, String> UrlParamToMap(String paramStr, String nonce) {
//        Map<String, String> paramMap = new HashMap<String, String>();
        Map<String, String> paramMap = new TreeMap<>();// 默认升序排序

        String[] paramArray = paramStr.split("&");
        for (String item : paramArray) {
            if (!IsStringNull(item)) {
                if (item.contains("=")) {
                    String[] valueArray = item.split("=");
                    String keyName = valueArray[0];
                    String value = "";
                    if (valueArray.length == 2) {
                        value = valueArray[1];
                    }

                    paramMap.put(keyName, value);
                }
            }
        }
        paramMap.put("nonce", nonce);
        return paramMap;
    }

    /**
     * 判断空字符串
     * 字符串为“null”时也判断为空
     *
     * @param str
     * @return
     */
    public static boolean IsStringNull(String str) {
        if (str == null || str.trim().length() == 0 || "null".equalsIgnoreCase(str)) {
            return true;
        }

        return false;
    }

//    /**
//     * 登录过期
//     *
//     * @param msg
//     */
//    public static void showLogoutDialog(String msg) {
//        new MaterialDialog.Builder(QMallApplication.getInstance())
//                .title("提示")
//                .content(msg)
//                .cancelable(false)
//                .positiveColor(QMallApplication.getInstance().getResources().getColor(R.color.color_F84747))
//                .positiveText("重新登录")
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        ActivityManager.getInstance().startLogin();
//                    }
//                })
//                .show();
//    }

    /**
     * 复制内容到剪切板
     *
     * @param context
     * @param copyStr
     */
    public static void clipboardCopyText(Context context, String copyStr) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", copyStr);
        cm.setPrimaryClip(mClipData);

    }

    /**
     * 拨号
     *
     * @param context
     * @param phone
     */
    public static void makePhoneCall(Context context, String phone) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");// 调用android自带的拨号Activity
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

}

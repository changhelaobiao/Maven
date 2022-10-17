package com.liaopeixin.lib_base.utils;

import android.content.Context;

/**
 * @author huanghuaqiao
 * @date 2021/11/16
 * 获取上下文的全局工具类
 */
public class ContextUtil {

    private static Context mContext;

    public static void init(Context context){
        mContext=context;
    }

    public static Context getContext(){
        return mContext;
    }


}

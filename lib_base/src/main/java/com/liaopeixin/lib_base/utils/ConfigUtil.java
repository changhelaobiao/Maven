package com.liaopeixin.lib_base.utils;

/**
 * @author huanghuaqiao
 * @date 2021/12/31
 * 全局app环境工具类
 */
public class ConfigUtil {

    private static ConfigUtil mConfigUtil;

    private static boolean isPro=false;
    private static String appEve;
    private static String url;

    public static ConfigUtil getInstance() {
        if (mConfigUtil == null) {
            synchronized (BuildConfigUtil.class) {
                if (mConfigUtil == null) {
                    mConfigUtil = new ConfigUtil();
                }
            }
        }
        return mConfigUtil;
    }

    public boolean isPro() {
        return isPro;
    }

    public ConfigUtil setIsPro(boolean isPro) {
        ConfigUtil.isPro = isPro;
        return this;
    }

    public String getAppEve() {
        return appEve;
    }

    public ConfigUtil setAppEve(String appEve) {
        ConfigUtil.appEve = appEve;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ConfigUtil setUrl(String url) {
        ConfigUtil.url = url;
        return this;
    }
}

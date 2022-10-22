package com.liaopeixin.lib_utils;

import android.app.Application;
import android.os.Parcelable;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;

import java.util.Set;

/**
 * author : toby
 * e-mail : 16620129640@163.com
 * time : 2022/9/2
 * desc : MMKV帮助类
 */
public class MMKVHelper {

    public static String init(Application context) {
        return MMKV.initialize(context);
    }

    private static MMKV getMMKVByGroup(String groupId, boolean multiProgress) {
        int mode = multiProgress ? MMKV.MULTI_PROCESS_MODE : MMKV.SINGLE_PROCESS_MODE;
        MMKV mmkv;
        if (TextUtils.isEmpty(groupId)) {
            mmkv = MMKV.defaultMMKV(mode, null);
        } else {
            mmkv = MMKV.mmkvWithID(groupId, mode);
        }
        return mmkv;
    }

    public static boolean put(String key, boolean value, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.encode(key, value);
    }

    public static boolean get(String key, boolean defValue, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.decodeBool(key, defValue);
    }

    public static boolean put(String key, int value, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.encode(key, value);
    }

    public static int get(String key, int defValue, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.decodeInt(key, defValue);
    }

    public static boolean put(String key, long value, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.encode(key, value);
    }

    public static long get(String key, long defValue, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.decodeLong(key, defValue);
    }

    public static boolean put(String key, float value, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.encode(key, value);
    }

    public static float get(String key, float defValue, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.decodeFloat(key, defValue);
    }

    public static boolean put(String key, double value, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.encode(key, value);
    }

    public static double get(String key, double defValue, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.decodeDouble(key, defValue);
    }

    public static boolean put(String key, byte[] value, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.encode(key, value);
    }

    public static byte[] get(String key, byte[] defValue, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.decodeBytes(key, defValue);
    }

    public static boolean put(String key, String value, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.encode(key, value);
    }

    public static String get(String key, String defValue, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.decodeString(key, defValue);
    }

    public static boolean put(String key, Set<String> value, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.encode(key, value);
    }

    public static Set<String> get(String key, Set<String> defValue, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.decodeStringSet(key, defValue);
    }

    public static boolean put(String key, Parcelable value, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.encode(key, value);
    }

    public static <T extends Parcelable> T get(String key, Class<T> tClass, T defValue, String groupId, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        return mmkv.decodeParcelable(key, tClass, defValue);
    }

    public static boolean delete(String groupId, String deleteItemKey, boolean multiProgress) {
        MMKV mmkv = getMMKVByGroup(groupId, multiProgress);
        mmkv.remove(deleteItemKey);
        return mmkv.contains(deleteItemKey);
    }

}

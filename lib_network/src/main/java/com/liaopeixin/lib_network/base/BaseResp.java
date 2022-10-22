/*
 * 文件名: BaseResp.java
 * 版 权： Copyright Co. Ltd. All Rights Reserved.
 * 创建时间: 2015-6-16
 */
package com.liaopeixin.lib_network.base;

/**
 * 基础响应数据, 解析code和msg
 */
public class BaseResp {

    /**
     * 操作成功
     */
    public static final int OK = 1;


    /**
     * Token错误
     */
    public static final int TOKEN_ERROR = 17;
    /**
     * 密码错误超过3次
     */
    public static final int PWD_ERROR_COUNT = 132;
    /**
     * 没有权限
     */
    public static final int CODE_FORBID = 16;
    /**
     * 课程-重新订阅
     */
    public static final int CODE_COURSE_RESUBSCRIBE = 160;

    private Integer code;
    private String msg;

    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * V屏使用, 请勿删除
     */
    private String md5ForVScreen;

    public int getCode() {
        if (code != null) {
            return code;
        }
        return 0;
    }

    public String getMessage() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMd5ForVScreen() {
        return md5ForVScreen;
    }

    public void setMd5ForVScreen(String md5ForVScreen) {
        this.md5ForVScreen = md5ForVScreen;
    }
}
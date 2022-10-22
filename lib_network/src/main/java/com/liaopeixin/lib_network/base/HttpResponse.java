/*
 * 文件名: Response.java
 * 版 权： Copyright Co. Ltd. All Rights Reserved.
 * 创建时间: 2015-6-16
 */
package com.liaopeixin.lib_network.base;

/**
 * 响应
 */
public class HttpResponse {

    /**
     * 失败
     */
    public static final int BUSINESS_CODE_NOK = 0;
    /**
     * 成功
     */
    public static final int BUSINESS_CODE_OK = 1;

    /**
     * 业务码
     */
    private int mBusinessCode = BUSINESS_CODE_NOK;
    private HttpRequest mHttpRequestInfo;
    private Object mData;
    private String message;
    private int code;

    public HttpRequest getRequestInfo() {
        return mHttpRequestInfo;
    }

    public void setRequestInfo(HttpRequest httpRequestInfo) {
        this.mHttpRequestInfo = httpRequestInfo;
    }

    public Object getData() {
        return mData;
    }

    public void setData(Object data) {
        this.mData = data;
    }

    public int getBusinessCode() {
        return mBusinessCode;
    }

    public void setBusinessCode(int businessCode) {
        this.mBusinessCode = businessCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public interface ResponseListener {

        void onResponse(HttpResponse httpResponse);
    }
}
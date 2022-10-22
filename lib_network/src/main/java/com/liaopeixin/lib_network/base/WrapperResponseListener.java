/*
 * 文件名: WrapperResponseListener.java
 * 版 权： Copyright Co. Ltd. All Rights Reserved.
 * 创建时间: 2013-1-28
 */
package com.liaopeixin.lib_network.base;

import android.util.Log;

/**
 * ResponseListener嵌套包装
 */
public class WrapperResponseListener implements HttpResponse.ResponseListener {
    public static final String TAG = "OkHttpUtil";
    private HttpResponse.ResponseListener mMainListener;
    private HttpResponse.ResponseListener mWrappedListener;

    /**
     * @param mainListener    主回调
     * @param wrappedListener 副回调
     */
    public WrapperResponseListener(HttpResponse.ResponseListener mainListener, HttpResponse.ResponseListener wrappedListener) {
        this.mMainListener = mainListener;
        this.mWrappedListener = wrappedListener;
    }

    @Override
    public void onResponse(HttpResponse httpResponse) {
        if (true) {
            if (mMainListener != null) {
                mMainListener.onResponse(httpResponse);
            }
            if (mWrappedListener != null) {
                mWrappedListener.onResponse(httpResponse);
            }
        } else {
            try {
                if (mMainListener != null) {
                    mMainListener.onResponse(httpResponse);
                }
                if (mWrappedListener != null) {
                    mWrappedListener.onResponse(httpResponse);
                }
            } catch (Exception ex) {
                //记录错误日志到文件里面
                HttpRequest httpRequest = httpResponse.getRequestInfo();
                if (httpRequest != null) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("http request_id:").append(httpRequest.getRequestId());
                    if (httpResponse.getData() != null) {
                        builder.append("\n");
                        builder.append("http response error :" + ex.toString());
                    }
                    Log.d(TAG, builder.toString());
                } else {
                    Log.d(TAG, " http error :" + ex.toString());
                }
            }
        }
    }
}
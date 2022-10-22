/*
 * 文件名: SoftResponseListener.java
 * 版 权： Copyright Co. Ltd. All Rights Reserved.
 * 创建时间: 2013-4-30
 */
package com.liaopeixin.lib_network.base;

import java.lang.ref.WeakReference;

/**
 * 软引用的ResponseListener
 */
public class WeakRefResponseListener implements HttpResponse.ResponseListener {

    private WeakReference<HttpResponse.ResponseListener> mResponseListenerRef;

    public WeakRefResponseListener(HttpResponse.ResponseListener responseListener) {
        this.mResponseListenerRef = new WeakReference<HttpResponse.ResponseListener>(responseListener);
    }

    @Override
    public void onResponse(HttpResponse httpResponse) {
        HttpResponse.ResponseListener responseListener = mResponseListenerRef.get();
        if (responseListener != null) {
            responseListener.onResponse(httpResponse);
        }
    }
}
/*
 * 文件名: ManagerBase.java
 * 版 权： Copyright Co. Ltd. All Rights Reserved.
 * 创建时间: 2013-2-20
 */
package com.liaopeixin.lib_network;

import android.content.Context;

import com.liaopeixin.lib_network.base.BaseResp;
import com.liaopeixin.lib_network.base.HttpRequest;
import com.liaopeixin.lib_network.base.HttpResponse;
import com.liaopeixin.lib_utils.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 管理器基类
 */
public abstract class ManagerBase implements HttpResponse.ResponseListener {

    protected IRetrofitEngineProxy mProxy;
    private Context mContext;
    protected HttpResponse.ResponseListener mListener;
    private static final String CHAR_CACHE = "cache_";
    private static final String DEVIDER = "_";

    public IRetrofitEngineProxy sRetrofitProxy;

    public IRetrofitEngineProxy getRetrofitProxy(Context context) {
        if (sRetrofitProxy == null) {
            sRetrofitProxy = new MGRetrofitEngineProxy();
//            sRetrofitProxy.setDebug(isDebug());
//            sRetrofitProxy.writeLogFileEnable(isWriteLogFileEnable());
        }
        return sRetrofitProxy;
    }

    public ManagerBase(Context context) {
        this.mContext = context;
        mProxy = getRetrofitProxy(context);
    }

    public void agent(HttpResponse.ResponseListener listener) {
        mListener = listener;
        mProxy = proxyRetrofit(mProxy);
    }

    protected final Context getContext() {
        return this.mContext;
    }


    public <T> T generateService(Class<T> cls) {
        return mProxy.createHttpServer("http:www.baidu.com",
                getHttpRequestHeaders(mContext), cls);
    }

    public <T> T generateService(Class<T> cls, String serverUrl) {
        return mProxy.createHttpServer(serverUrl,
                getHttpRequestHeaders(mContext), cls);
    }

    public static Map<String, String> getHttpRequestHeaders(Context context) {
        return new HashMap<>();
    }

    private IRetrofitEngineProxy proxyRetrofit(final IRetrofitEngineProxy object) {
        return (IRetrofitEngineProxy) Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();

                if (!methodName.startsWith("enqueue")) {
                    return method.invoke(object, args);
                }
                if (mListener != null) {
                    args[3] = mListener;
                }
                return method.invoke(object, args);
            }
        });
    }

    protected HttpResponse.ResponseListener getResponseProxy(final HttpResponse.ResponseListener listener) {
        return (HttpResponse.ResponseListener) Proxy.newProxyInstance(listener.getClass().getClassLoader(), listener.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                HttpResponse response = (HttpResponse) args[0];
                Object object = response.getData();
                if (object instanceof BaseResp) {
                    BaseResp resp = (BaseResp) response.getData();
                    HttpRequest requestInfo = response.getRequestInfo();
                    if (resp == null || resp.getCode() != BaseResp.OK) {
                        return method.invoke(listener, args);
                    }
                    object = requestInfo.getData();
                    if (object instanceof CacheEntity) {
                        CacheEntity data = (CacheEntity) requestInfo.getData();
                        if (data == null) {
                            return method.invoke(listener, args);
                        }
//                        CommHttpApp.cacheContentToJson(mContext, data.userId, data.fileName, response.getData());
                    }
                }
                return method.invoke(listener, args);
            }
        });
    }

    public static CacheEntity generateFileName(String userId, String... parmas) {
        StringBuilder sb = new StringBuilder();
        for (String parma : parmas) {
            if (StringUtils.isEmpty(parma)) {
                continue;
            }
            sb.append(parma);
            sb.append(DEVIDER);
        }
        if (sb.length() < 0) {
            return null;
        }

        sb.insert(0, CHAR_CACHE);
        sb.deleteCharAt(sb.length() - 1);

        CacheEntity entity = new CacheEntity();
        entity.userId = userId;
        entity.fileName = sb.toString();
        return entity;
    }


    public static class CacheEntity {
        //缓存字段，主要用于生成文件名，
        public String userId;
        public String fileName;
        public String versionNo;
        public long time;
    }

    @Override
    public void onResponse(HttpResponse response) {
        Object object = response.getData();
        if (object instanceof BaseResp) {
            BaseResp resp = (BaseResp) response.getData();
            HttpRequest requestInfo = response.getRequestInfo();
            if (resp == null || resp.getCode() != BaseResp.OK) {
                return;
            }
            object = requestInfo.getData();
            if (object instanceof CacheEntity) {
                CacheEntity data = (CacheEntity) requestInfo.getData();
                if (data == null || data.fileName == null) {
                    return;
                }
//                CommHttpApp.cacheContentToJson(mContext, data.userId, data.fileName, response.getData());
            }
        }
    }

    public void setInfoAndEnqueue(Call call, int callId, Object requestInfo, HttpResponse.ResponseListener listener) {
        HttpRequest request = new HttpRequest();
        if (requestInfo != null) {
            request.setData(requestInfo);
        }
        request.setRequestId(callId);
        if (mProxy != null) {
            mProxy.enqueue(call, getContext(), request, this, listener);
        }
    }
}
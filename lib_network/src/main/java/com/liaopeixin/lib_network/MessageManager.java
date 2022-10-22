package com.liaopeixin.lib_network;

import android.content.Context;

import com.liaopeixin.lib_network.base.HttpRequest;
import com.liaopeixin.lib_network.base.HttpResponse;
import com.liaopeixin.lib_network.base.MessageResp;

import retrofit2.Call;

/**
 * author : toby
 * e-mail : 16620129640@163.com
 * time : 2022/9/4
 * desc :
 */
public class MessageManager extends ManagerBase {

    private static MessageManager sInstance;
    private static Context mContext;
    private static IRetrofitEngineProxy mProxy;

    public MessageManager(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        mProxy = getRetrofitProxy(context);
    }

    public static MessageManager get(Context context) {
        mContext = context;
        if (sInstance == null) {
            synchronized (MessageManager.class) {
                if (sInstance == null) {
                    sInstance = new MessageManager(context);
                }
                getServer();
            }
        }
        return sInstance;
    }

    public static MessageServer getServer() {
        return mProxy.createHttpServer("https://www.wanandroid.com",
                getHttpRequestHeaders(mContext), MessageServer.class);
    }

    public Call<MessageResp> getData1(HttpResponse.ResponseListener listener) {
        HttpRequest request = new HttpRequest();
        request.setRequestId(1000);
        Call<MessageResp> call = getServer().getData1();
        if (call != null) {
            mProxy.enqueue(call, getContext(), request, this, listener);
        }
        return call;
    }

}

package com.liaopeixin.lib_network;

import android.content.Context;

import com.liaopeixin.lib_network.base.HttpRequest;
import com.liaopeixin.lib_network.base.HttpResponse;
import com.liaopeixin.lib_network.base.WrapperResponseListener;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class MGRetrofitEngineProxy implements IRetrofitEngineProxy {
    private boolean mIsDebug = true;
    private boolean mWriteLogFileEnable = false;

    @Override
    public <T> T createHttpServer(String url, Map<String, String> headers, Class<T> service, long connectTime, long readTime, long writeTime) {
        RetrofitWrapper wrapper = RetrofitWrapper.getInstance(url, headers, mIsDebug, mWriteLogFileEnable, connectTime, readTime, writeTime);
        return wrapper.create(service);
    }

    @Override
    public <T> T createHttpServer(String url, Map<String, String> headers, Class<T> service) {
        RetrofitWrapper wrapper = RetrofitWrapper.getInstance(url, headers, mIsDebug, mWriteLogFileEnable);
        return wrapper.create(service);
    }

    @Override
    public <T> T execute(Call<T> t) {
        if (t != null) {
            try {
                Response<T> response = t.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    protected void processNetWorkError(Context context) {
    }

    @Override
    public <T> void enqueue(Call<T> call, Context context, HttpRequest httpRequest, HttpResponse.ResponseListener mainListener, HttpResponse.ResponseListener listener) {
        try {
            processNetWorkError(context);
            MGRJsonRequest<T> callback;
            if (mainListener != null) {
                callback = new MGRJsonRequest<T>(context, httpRequest, new WrapperResponseListener(mainListener, listener));
            } else {
                callback = new MGRJsonRequest<T>(context, httpRequest, listener);
            }
            call.enqueue(callback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setDebug(boolean isDebug) {
        this.mIsDebug = isDebug;
    }

    @Override
    public void writeLogFileEnable(boolean isEnable) {
        this.mWriteLogFileEnable = isEnable;
    }
}

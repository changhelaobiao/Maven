package com.liaopeixin.lib_network;

import android.content.Context;

import com.liaopeixin.lib_network.base.HttpRequest;
import com.liaopeixin.lib_network.base.HttpResponse;

import java.util.Map;

import retrofit2.Call;

public interface IRetrofitEngineProxy {
    <T> T createHttpServer(String url, Map<String, String> headers, Class<T> service, long connectTime, long readTime, long writeTime);

    <T> T createHttpServer(String url, Map<String, String> headers, Class<T> service);

    <T> T execute(Call<T> t);

    <T> void enqueue(Call<T> call, Context context, HttpRequest httpRequest, HttpResponse.ResponseListener mainListener, HttpResponse.ResponseListener listener);

    void setDebug(boolean isDebug);

    void writeLogFileEnable(boolean isEnable);
}

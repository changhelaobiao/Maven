package com.liaopeixin.lib_network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitWrapper 一些资源常驻池
 */
public final class RetrofitWrapperPool {
    private static final String TAG = "RetrofitWrapperPool";
    private static RetrofitWrapperPool mOurInstance;
    private GsonConverterFactory mGsonConverterFactory;


    public static RetrofitWrapperPool get() {
        if (mOurInstance == null) {
            synchronized (RetrofitWrapperPool.class) {
                if (mOurInstance == null) {
                    mOurInstance = new RetrofitWrapperPool();
                }
            }
        }
        return mOurInstance;
    }

    private RetrofitWrapperPool() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        mGsonConverterFactory = GsonConverterFactory.create(gson);
    }

    public void dispose() {
        //to implement your project
    }

    public GsonConverterFactory getGsonConverterFactory() {
        return mGsonConverterFactory;
    }

}

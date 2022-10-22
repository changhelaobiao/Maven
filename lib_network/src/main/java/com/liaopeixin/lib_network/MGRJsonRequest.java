package com.liaopeixin.lib_network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.liaopeixin.lib_network.base.HttpErrorCode;
import com.liaopeixin.lib_network.base.HttpRequest;
import com.liaopeixin.lib_network.base.HttpResponse;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Retrofit 请求管理类
 */
public class MGRJsonRequest<T> implements Callback<T> {
    public static final String TOKEN_ID = "tokenId";
    public static final String CLIENT_ID = "clientId";
    public static final String TAG = "R_HTTP";
    public static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    private Context mContext;
    protected HttpResponse.ResponseListener mListener;
    protected HttpResponse.ResponseListener mDeliverResponseListener;
    protected HttpRequest mHttpRequestInfoData;

    public static RequestBody getRequestBody(String content) {
        return RequestBody.create(okhttp3.MediaType.parse(CONTENT_TYPE), content);
    }

    public MGRJsonRequest(Context context, HttpRequest httpRequest, HttpResponse.ResponseListener listener) {
        this.mContext = context;
        this.mHttpRequestInfoData = httpRequest;
        this.mListener = listener;
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        if (throwable == null) {
            return;
        }
        Log.d(TAG, throwable.toString());
        if (mListener == null) {
            return;
        }
        int code;
        if (throwable instanceof UnknownHostException) {
            ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo active = null;
            if (connMgr != null) {
                active = connMgr.getActiveNetworkInfo();
            }
            if (active == null || !active.isConnectedOrConnecting()) {
                code = HttpErrorCode.NO_NETWORK;
            } else {
                code = HttpErrorCode.NETWORK_EXCEPTION;
            }
        } else if (throwable instanceof ConnectException) {
            code = HttpErrorCode.NETWORK_BROKEN;
        } else if (throwable instanceof TimeoutException) {
            code = HttpErrorCode.NETWORK_TIMEOUT;
        } else {
            code = HttpErrorCode.ACTION_FAILED;
        }
        HttpResponse response = new HttpResponse();
        response.setBusinessCode(HttpResponse.BUSINESS_CODE_NOK);
        response.setCode(code);
        if (mHttpRequestInfoData != null) {
            response.setRequestInfo(mHttpRequestInfoData);
            Log.d(TAG, "onFailure: request data  " + mHttpRequestInfoData.toString());
        }
        mListener.onResponse(response);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response == null) {
            return;
        }
        T t = response.body();
        HttpResponse info;
        if (t == null) {
            if (mListener != null) {
                info = new HttpResponse();
                info.setBusinessCode(HttpResponse.BUSINESS_CODE_NOK);
                info.setCode(HttpErrorCode.ACTION_FAILED);
                info.setRequestInfo(mHttpRequestInfoData);
                mListener.onResponse(info);
            }
            return;
        }
        if (mListener == null) {
            return;
        }
        info = new HttpResponse();
        info.setBusinessCode(HttpResponse.BUSINESS_CODE_OK);
        info.setCode(HttpErrorCode.SUCCESS);
        info.setData(t);
        if (mHttpRequestInfoData != null) {
            info.setRequestInfo(mHttpRequestInfoData);
        }
        mListener.onResponse(info);
    }
}

package com.liaopeixin.lib_network;

import com.liaopeixin.lib_network.base.HttpRequest;
import com.liaopeixin.lib_network.base.MessageResp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * author : toby
 * e-mail : 16620129640@163.com
 * time : 2022/9/4
 * desc :
 */
public interface MessageServer {

    /**
     * get请求
     */
    @GET("/navi/json")
    Call<MessageResp> getData1();

    /**
     * post请求
     */
    @POST("/business/daily/score/add")
    Call<MessageResp> updateData1(@Body HttpRequest data);
}

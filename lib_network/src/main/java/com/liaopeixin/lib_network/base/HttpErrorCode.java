package com.liaopeixin.lib_network.base;

public interface HttpErrorCode {
    /**
     * 无可用网络
     */
    int NO_NETWORK = 0;
    /**
     * 无法连接到服务器
     */
    int NETWORK_EXCEPTION = 1;
    /**
     * 当前网络不可用
     */
    int NETWORK_BROKEN = 2;
    /**
     * 网络超时
     */
    int NETWORK_TIMEOUT = 3;
    /**
     * 请求失败
     */
    int ACTION_FAILED = 4;

    /**
     * 请求成功
     */
    int SUCCESS = 5;
}

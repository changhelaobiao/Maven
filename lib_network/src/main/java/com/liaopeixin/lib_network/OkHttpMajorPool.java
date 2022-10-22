package com.liaopeixin.lib_network;

import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;

/**
 * OkHttp 主要的资源池管理
 */
public final class OkHttpMajorPool {
    private static OkHttpMajorPool mOurInstance;
    private Dispatcher mDispatcher;
    private ConnectionPool mConnectionPool;

    public static OkHttpMajorPool get() {
        if (mOurInstance == null) {
            synchronized (OkHttpMajorPool.class) {
                if (mOurInstance == null) {
                    mOurInstance = new OkHttpMajorPool();
                }
            }
        }
        return mOurInstance;
    }

    public void dispose() {
        //to implement your project
    }

    private OkHttpMajorPool() {
        //OkHttp执行异步请求的策略。 内部实现了一个"OkHttp Dispatcher"无边界的线程池
        mDispatcher = new Dispatcher();
        //maxRequests 当前okhttpclient实例最大的并发请求数
        //默认：64，默认的64一般满足不了业务需要。这个值一般要大于maxRequestPerHost，如果这个值小于maxRequestPerHost会导致，请求单个主机的并发不可能超过maxRequest.
        //mDispatcher.setMaxRequests(64);  //to implement your project
        //单个主机最大请求并发数，这里的主机指被请求方主机，一般可以理解对调用方有限流作用。注意：websocket请求不受这个限制。
        //默认：4，一般建议与maxRequest保持一致。这个值设置，有如下几个场景考虑：（1）如果被调用方的并发能力只能支持200，那这个值最好不要超过200，否则对调用方有压力；
        //（2）如果当前okhttpclient实例只对一个调用方发起调用，那这个值与maxRequests保持一致；
        //（3）如果当前okhttpclient实例在一个事务中对n个调用方发起调用，n * maxReuestPerHost要接近maxRequest
        //mDispatcher.setMaxRequestsPerHost(4);//to implement your project

        //连接池: Okhttp支持5个并发KeepAlive，默认链路生命为5分钟(链路空闲后，保持存活的时间)，连接池有ConectionPool实现，对连接进行回收和管理。"OkHttp ConnectionPool" 后台守护线程池
        mConnectionPool = new ConnectionPool();
    }

    public Dispatcher getDispatcher() {
        return mDispatcher;
    }

    public ConnectionPool getConnectionPool() {
        return mConnectionPool;
    }
}

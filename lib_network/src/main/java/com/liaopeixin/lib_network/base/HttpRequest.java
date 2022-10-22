/*
 * 文件名: Request.java
 * 版 权： Copyright Co. Ltd. All Rights Reserved.
 * 创建时间: 2015-6-16
 */
package com.liaopeixin.lib_network.base;

/**
 * 请求
 */
public class HttpRequest {
    public HttpRequest(int requestId) {
        this.requestId = requestId;
    }

    public HttpRequest() {
    }
    /**
     * 请求的业务Id, 能够通过此Id知道具体的业务
     */
    private int requestId;

    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 请求的业务数据
     */
    private Object data;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
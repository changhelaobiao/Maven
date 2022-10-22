package com.liaopeixin.lib_network.base;

import java.util.List;

/**
 * author : toby
 * e-mail : 16620129640@163.com
 * time : 2022/9/4
 * desc :
 */
public class MessageResp extends BaseResp{

    private String errorMsg;
    private int errorCode;
    private List<ArticleData> data;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<ArticleData> getData() {
        return data;
    }

    public void setData(List<ArticleData> data) {
        this.data = data;
    }

    public class ArticleData{
        private String cid;
        private String name;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

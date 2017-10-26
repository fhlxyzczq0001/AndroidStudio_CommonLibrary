package com.wzgiceman.rxretrofitlibrary.retrofit_rx.downlaod;

import com.wzgiceman.rxretrofitlibrary.retrofit_rx.listener.HttpDownOnNextListener;

/**
 * apk下载请求数据基础类
 * Created by WZG on 2016/10/20.
 */

public class DownInfo {
    /*存储位置*/
    private String savePath;
    /*文件总长度*/
    private long countLength;
    /*下载长度*/
    private long readLength;
    /*下载回调监听*/
    private HttpDownOnNextListener listener;
    /*超时设置*/
    private  int connectonTime=6;
    /*url*/
    private String url;

    public DownInfo(String downUrl,String saveUrl, HttpDownOnNextListener listener) {
        setUrl(downUrl);
        setSavePath(saveUrl);
        setListener(listener);
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public long getCountLength() {
        return countLength;
    }

    public void setCountLength(long countLength) {
        this.countLength = countLength;
    }

    public long getReadLength() {
        return readLength;
    }

    public void setReadLength(long readLength) {
        this.readLength = readLength;
    }

    public HttpDownOnNextListener getListener() {
        return listener;
    }

    public void setListener(HttpDownOnNextListener listener) {
        this.listener = listener;
    }

    public int getConnectonTime() {
        return connectonTime;
    }

    public void setConnectonTime(int connectonTime) {
        this.connectonTime = connectonTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

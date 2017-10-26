package com.wzgiceman.rxretrofitlibrary.retrofit_rx.cookiejar;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2017/2/23.
 */

public class CookiesManager implements CookieJar {
    Context mContext;
    private final PersistentCookieStore cookieStore;
    public CookiesManager(Context context,String urlHost){
        mContext=context;
        cookieStore = new PersistentCookieStore(mContext,urlHost);
    }
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url);
        return cookies;
    }
}

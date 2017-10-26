package com.utlis.lib;

/**
 * @ClassName:com.ygworld.Util
 * @author: Administrator 闫亚琴
 * @date: 2017/5/16 9:49
 */
public class StringUtils {
    /**
     * 截取url给微信链接设置cookie
     *
     * @param url 传入当前url
     * @return 将截取的url返回
     */
    public static String urlSetCookie(String url) {
        String wxUrl = url;
        int index = url.indexOf("/", 7);
        if (index > 0) {
            wxUrl = url.substring(0, index) + "/";
        }
        return wxUrl;
    }
}

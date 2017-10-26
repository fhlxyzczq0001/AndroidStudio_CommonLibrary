package com.utlis.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * @ClassName: com.ygworld.Util
 * @author: Administrator 杨重诚
 * @date: 2016/8/31:15:26
 */
public class BitmapUtils {
    /**
     * 字节编码转图片
     * @param string
     * @return
     */
    public static Bitmap stringtoBitmap(String string){
        //将字符串转换成Bitmap类型
        Bitmap bitmap=null;
        try {
            byte[] bitmapArray= Base64.decode(string, Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}

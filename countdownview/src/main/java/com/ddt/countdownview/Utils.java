package com.ddt.countdownview;

import android.content.Context;

/**
 * Utils
 * Created by iWgang on 16/6/19.
 * https://github.com/iwgang/CountdownView
 */
final class Utils {

    public static int dp2px(Context context, float dpValue) {
        if (dpValue <= 0) return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static float sp2px(Context context, float spValue) {
        if (spValue <= 0) return 0;
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * scale;
    }

    public static String formatNum(int time) {
        return time < 10 ? "0" + time : String.valueOf(time);
    }
    public static String[] formatNumBackground(int time) {
        return time < 10 ? new String[]{"0",String.valueOf(time)} : new String[]{String.valueOf(time/10),String.valueOf(time%10)};
    }

    public static String formatMillisecond(int millisecond) {
        String retMillisecondStr;

        if (millisecond > 99) {
            retMillisecondStr = String.valueOf(millisecond / 10);
        } else if (millisecond <= 9) {
            retMillisecondStr = "0" + millisecond;
        } else {
            retMillisecondStr = String.valueOf(millisecond);
        }

        return retMillisecondStr;
    }
    public static String[] formatMillisecondBackground(int millisecond) {
        String retMillisecondStr;
        String retMillisecondStr1;

        if (millisecond > 99) {
        	int baseMillisecondStr = millisecond / 10;
            retMillisecondStr = String.valueOf(baseMillisecondStr/10);
            retMillisecondStr1 = String.valueOf(baseMillisecondStr%10);
        } else if (millisecond <= 9) {
            retMillisecondStr = "0";
            retMillisecondStr1 = String.valueOf(millisecond);
        } else {
            retMillisecondStr = String.valueOf(millisecond/10);
            retMillisecondStr1 = String.valueOf(millisecond%10);
        }

        return new String[]{retMillisecondStr,retMillisecondStr1};
    }

}

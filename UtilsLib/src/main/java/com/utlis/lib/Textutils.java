package com.utlis.lib;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Textutils {
    /**
     * 获取View内容
     *
     * @param ed
     * @return
     * @Title: returnEditText
     * @Description: TODO
     * @return: String
     */
    public static String getEditText(View ed) {
        if (ed != null) {
            return ((TextView) ed).getText().toString().trim();
        }
        return null;
    }

    /**
     * 检测Text为空显示默认
     *
     * @param getString
     * @return
     * @Title: checkText
     * @Description: TODO
     * @return: String
     */
    public static String checkText(String getString) {

        if (getString != null && !getString.equals("null") && !getString.equals("") && getString.length() > 0) {

            return getString.trim().toString();

        } else {

            return "暂无";

        }

    }

    /**
     * 获取Values下的String内容
     *
     * @param context
     * @param resId
     * @return
     */
    public static String getValuesString(Context context, int resId) {
        return checkText(context.getResources().getString(resId));
    }

    /**
     * 把字符串转换为十六进制的颜色编码
     */
    public static String colorShiftUtil(String str) {
        if (str == null || str.equals("")) {
            return "#dd2727";
        }
        if (str.startsWith("#")) {
            return str;
        } else if (str.contains(",")) {
            if (str.contains("(")) {
                int start = str.indexOf("(");
                int end = str.lastIndexOf(")");
                str = str.substring(start + 1, end);
            }
            String[] rgb = str.split(",");
            if (rgb.length == 3) {
                return toHexEncoding(Integer.valueOf(rgb[0].trim()),
                        Integer.valueOf(rgb[1].trim()), Integer.valueOf(rgb[2].trim()));
            } else if (rgb.length == 4) {
                return toHexEncoding(Integer.valueOf(rgb[1].trim()),
                        Integer.valueOf(rgb[2].trim()), Integer.valueOf(rgb[3].trim()));
            }
        }
        return "#000000";
    }

    public static String toHexEncoding(int red, int green, int blue) {
        String a, r, g, b;
        r = Integer.toHexString(red);
        g = Integer.toHexString(green);
        b = Integer.toHexString(blue);
        StringBuffer sb = new StringBuffer();
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        sb.append("#");
        sb.append(r.toUpperCase());
        sb.append(g.toUpperCase());
        sb.append(b.toUpperCase());
        return sb.toString();
    }

    /**
     * 长度为8到20位,由英文、数字组成,且不能为纯数字
     *
     * @param paramString
     * @return
     */
    public static boolean checkName8_20(String paramString) {
        boolean result = Pattern
                .compile("(?!^(\\d+|[a-zA-Z]+|[~!@#$%^&*?]+)$)^[\\w~!@#$%\\^&*?]{8,20}$")
                .matcher(paramString).matches();
        return result;
    }

    /**
     * 判断如果是null返回字符"null"
     *
     * @param str
     * @return
     */
    public static String isNull(String str) {
        if (str == null) {
            return "null";
        }
        return str;
    }

    /**
     * 字符串判空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        if(str.trim().equals("") || str.trim().equals("null") || str.trim().length() <= 0){
            return true;
        }
        return false;
    }

    /**
     * 返回新的银行卡样式----（4，8（*），4）
     *
     * @param bank
     * @return
     */
    public static String bankStyle(String bank) {
        if (bank.length() >= 13 || bank.length() <= 19) {
            bank = bank.substring(0, 4) + " **** **** "
                    + bank.substring(bank.length() - 4, bank.length());
        }
        return bank;
    }

    /**
     * 校验是否有表情符
     *
     * @param codePoint
     * @return
     */
    public static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 设置过滤表情符
     * @return
     */
    public static InputFilter[] emojiFilters(){
        InputFilter[] emojiFilters = {emojiFilter};
        return emojiFilters;
    }
    /**
     * 校验是否有表情符
     *
     * @param codePoint
     * @return
     */
    public static InputFilter emojiFilter = new InputFilter() {
        Pattern emoji = Pattern.compile(
                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart,
                                   int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                return "";
            }
            return null;
        }
    };
    /**
     * 校验字符串是否为空
     *
     * @param paramString
     * @return
     */
    public static boolean checkStringNoNull(String paramString) {
        return null != paramString && paramString.trim().length() > 0;
    }
}

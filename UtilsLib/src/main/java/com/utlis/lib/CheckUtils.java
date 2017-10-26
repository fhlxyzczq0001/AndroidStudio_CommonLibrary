package com.utlis.lib;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**校验工具类
 * @ClassName: com.ygworld.Util
 * @author: Administrator 杨重诚
 * @date: 2016/9/1:11:39
 */
public class CheckUtils {
    /**
     * 校验文件路径是否存在
     * @param path
     * @return
     */
    public static boolean checkFileExists(String path) {
        if (new File(path).exists()) {
            return true;
        }
        return false;
    }

    /**
     * 校验用户名或密码
     * @param paramString
     * @return
     */
    public static boolean checkName8_20(String paramString) {
        // 长度为8到18位,由英文、数字组成,且不能为纯数字
        boolean result = Pattern.compile("(?!^(\\d+|[a-zA-Z]+|[~!@#$%^&*?]+)$)^[\\w~!@#$%\\^&*?]{8,20}$").matcher(paramString).matches();
        return result;
    }

    /**
     * 校验手机号
     * @param paramString
     * @return
     */
    public static boolean chekPhone(String paramString) {
        return Pattern.compile("[1][345678]\\d{9}").matcher(paramString).matches();
    }

    public static boolean checkStringWithHtmlTag(String param) {
        String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>}
        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>}
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        boolean result = !Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE).matcher(param).matches();
        result = result && !Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE).matcher(param).matches();
        result = result && !Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE).matcher(param).matches();
        result = result && !param.contains("<") && !param.contains(">");
        return result;
    }
    /**
     * 返回true就是包含非法字符，返回false就是不包含非法字符 系统内容过滤规则 1、包含 『 and 1 特殊字符 』， 特殊字符指>,<,=,
     * in , like 字符 2、『 /特殊字符/ 』，特殊字符指 *字符 3、『<特殊字符 script 』特殊字符指空字符 4、『 EXEC 』
     * 5、『 UNION SELECT』 5、『 UPDATE SET』 5、『 INSERT INTO VALUES』 5、『
     * SELECT或DELETE FROM』 5、『CREATE或ALTER或DROP或TRUNCATE TABLE或DATABASE』
     */
    public static boolean isAttack(String input) {
        L.e("input:",input);
        String regExpr="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pat = Pattern.compile(regExpr);
        Matcher mat = pat.matcher(input);
        boolean rs = mat.find();
        if(rs){
            return false;
        }
        return true;
    }
    public static boolean isAttack2(String input) {
        L.e("input:",input);
        String regExpr="[`~@#$%^&*()+=|{}\\[\\].<>/~@#￥%……&*（）——+|{}【】]";
        Pattern pat = Pattern.compile(regExpr);
        Matcher mat = pat.matcher(input);
        boolean rs = mat.find();
        if(rs){
            return false;
        }
        return true;
    }
    /**
     * 校验字符串是否为空
     * @param paramString
     * @return
     */
    public static boolean checkStringNoNull(String paramString) {
        return null != paramString && paramString.trim().length() > 0;
    }

    /**
     * 检测该包名所对应的应用是否存在
     *
     * @param packageName
     * @return
     */
    public static boolean checkPackage(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES );
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
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
     * 中文校验
     * @param realName
     * @return
     */
    public static boolean isChinese(String realName) {
        return Pattern.compile("^[\u4E00-\u9FA5]{2,5}$|(?:·[\u4E00-\u9FA5]{2,5})$")
                .matcher(realName).matches();
    }

    /**
     * 校验身份证号码
     * @param cardId
     * @return
     */
    public static boolean isCard(String cardId) {
        // 身份证正则表达式(15位)
        Pattern isIDCard1 = Pattern
                .compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
        // 身份证正则表达式(18位)
        Pattern isIDCard2 = Pattern
                .compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
        Matcher matcher1 = isIDCard1.matcher(cardId);
        Matcher matcher2 = isIDCard2.matcher(cardId);
        boolean isMatched = matcher1.matches() || matcher2.matches();
        return isMatched;
    }

}

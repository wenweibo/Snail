package com.cqkj.publicframework.tool;

import java.util.regex.Pattern;

/**
 * 校验工具类
 */
public class ValidTool {
    /**
     * 是否是手机号
     * @param num 手机号码
     * @return
     */
    public static boolean isMobile(String num){
        Pattern pattern = Pattern.compile("\\d{11}");
        return pattern.matcher(num).matches();
    }

    /**
     * 是否是合法密码
     * @param password
     * @return
     */
    public static boolean validPassword(String password){
        Pattern pattern = Pattern.compile("[a-zA-z0-9]{6,12}");
        return pattern.matcher(password).matches();
    }
}

package com.luo.dubbo.util;

import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

public class PatternUtils {
    /***
     * 包名
     */
    private final static Pattern patternPackage = Pattern.compile("^([a-zA-Z]+[.][a-zA-Z]+)[.]*.*");
    private final static Pattern patternIP = Pattern
            .compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");

    /***
     * 验证包名
     * 
     * @param s
     * @return
     * @author HadLuo 2017年12月19日 新建
     */
    public static boolean isJavaPackage(String s) {
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        return patternPackage.matcher(s).find();
    }

    /***
     * 验证 127.0.0.1:8080 
     * @param address
     * @return
     * @author HadLuo  2017年12月19日 新建
     */
    public static boolean isAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            return false;
        }
        String[] strs = address.split(":");
        if (strs.length != 2) {
            return false;
        }
        if (!isIP(strs[0])) {
            return false;
        }
        if (!isPort(strs[1])) {
            return false;
        }
        return true;
    }

    public static boolean isIP(String s) {
        if (s == null || s.length() < 7 || s.length() > 15 || "".equals(s)) {
            return false;
        }
        return patternIP.matcher(s).find();
    }

    public static boolean isPort(String s) {
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        try {
            int port = Integer.parseInt(s);
            if (port < 1 || port > 65535) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

package com.hots.common.constant;

public class RegexConstant {

    // 中文字符
    public static final String CN_REG = "[\\u4e00-\\u9fa5]";
    public static final String NO_CN_REG = "[^\\u4e00-\\u9fa5]+";

    // 时间格式
    public static final String FORMAT_YEAR = "yyyy";
    public static final String FORMAT_DATE_NUM = "yyyyMMdd";
    public static final String FORMAT_DATE_NUM_FULL = "yyyyMMddHHmmssSSS";
    public static final String FORMAT_DATE_EN = "yyyy-MM-dd";
    public static final String FORMAT_DATE_CN = "yyyy年MM月dd日";
    public static final String FORMAT_TIME_EN = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_TIME_CN = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String FORMAT_TIME_FULL = "yyyy-MM-dd HH:mm:ss:SSS";

    // 网络访问
    public static final String IP_REG = "^((1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.){3}(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
}

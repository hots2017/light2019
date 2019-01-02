package com.hots.common.util;

import com.hots.common.constant.RegexConstant;
import com.hots.common.exception.BasicException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil  extends RegexConstant {
    /**
     * yyyy：年
     * MM：月
     * dd：日
     * hh：1~12小时制(1-12)
     * HH：24小时制(0-23)
     * mm：分
     * ss：秒
     * S：毫秒
     * E：星期几
     * D：一年中的第几天
     * F：一月中的第几个星期(会把这个月总共过的天数除以7)
     * w：一年中的第几个星期
     * W：一月中的第几星期(会根据实际情况来算)
     * a：上下午标识
     * k：和HH差不多，表示一天24小时制(1-24)。
     * K：和hh差不多，表示一天12小时制(0-11)。
     * z：表示时区
     */
    private static final SimpleDateFormat SDF = new SimpleDateFormat();

    public static Date strToDate(String timeFormat, String timeStr) {

        try {
            SDF.applyPattern(timeFormat);
            return SDF.parse(timeStr);
        } catch (Exception e) {
            new BasicException("时间格式化失败。", "需要格式化的时间：" + timeStr + ", 目标格式：" + timeFormat, e);
        }

        return null;
    }

    public static String format(String timeFormat, Date date) {
        SDF.applyPattern(timeFormat);
        return SDF.format(date);
    }

}

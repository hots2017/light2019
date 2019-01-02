package com.hots.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hots.common.constant.RegexConstant;
import com.hots.common.exception.BasicException;

public class StringUtil extends RegexConstant {

    /**
     * 判断字符串为空
     * 
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0 || str.trim().equalsIgnoreCase("null");
    }

    /**
     * 判断字符串全为数字
     * 
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        } else {
            return str.matches("^\\d+$");
        }
    }

    /**
     * 判断字符串可转为整型数据
     * 
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        if (isEmpty(str)) {
            return false;
        } else {
            return str.matches("^[+-]?\\d+$") && strToInt(str) != null;
        }
    }

    /**
     * 判断字符串可转为Float
     * 
     * @param str
     * @return
     */
    public static boolean isFloat(String str) {
        if (isEmpty(str)) {
            return false;
        } else {
            return str.matches("^[+-]?[\\d]+\\.[\\d]+$");
        }
    }

    /**
     * 判断字符串可转为Double
     * 
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {
        return isInteger(str) || isFloat(str);
    }

    /**
     * 字符串转Integer
     * 
     * @param str
     * @return
     */
    public static Integer strToInt(String str) {

        try {
            return new Integer(str);
        } catch (Exception e) {
            new BasicException("字符串转整形失败", str, e);
        }

        return null;
    }

    /**
     * 字符串第一个字符转大写
     * 
     * @param str
     * @return
     */
    public static String firstLetterToCapture(String str) {

        if (!isEmpty(str)) {

            String firstStr = str.substring(0, 1).toUpperCase();

            String elseStr = str.length() > 1 ? str.substring(1, str.length()) : "";

            return firstStr + elseStr;

        }
        return "";
    }

    /**
     * 字符串去重
     * 
     * @param str
     * @param splitFlg
     * @param tmpList
     * @return
     */
    public static String filterStr(String source, String split, List<String> tmpList) {

        if (!StringUtil.isEmpty(source)) {
            if (tmpList == null) {
                tmpList = new ArrayList<String>();
            } else {
                tmpList.clear();
            }

            for (String tmpStr : source.split("splitFlg")) {
                if (!tmpList.contains(tmpStr.trim())) {
                    tmpList.add(tmpStr);
                }
            }

            // 返回组合结果
            if (tmpList.size() > 0) {
                StringBuilder builder = new StringBuilder();
                for (String tmpStr : tmpList) {
                    if (!StringUtil.isEmpty(tmpStr)) {
                        builder.append(split).append(tmpStr);
                    }
                }
                if (builder.length() > 0) {
                    return builder.substring(split.length(), builder.length());
                }
            }
        }

        return "";
    }

    /**
     * 集合转字符串
     * 
     * @param target
     * @param split
     * @return
     */
    public static <T> String collectionToStr(Collection<T> target, String split) {

        StringBuilder builder = new StringBuilder();
        if (target != null && target.size() > 0) {
            for (T tmp : target) {

                String value = String.valueOf(tmp);

                if (!StringUtil.isEmpty(value)) {
                    builder.append(split).append(value);
                }
            }

            return builder.substring(split.length(), builder.length());
        }

        return builder.toString();
    }

    public static <T> String arrToStr(T[] target, String split) {

        StringBuilder builder = new StringBuilder();
        if (target != null && target.length > 0) {
            for (T tmp : target) {

                String value = String.valueOf(tmp);

                if (!StringUtil.isEmpty(value)) {
                    builder.append(split).append(value);
                }
            }

            return builder.substring(split.length(), builder.length());
        }

        return builder.toString();

    }

    /**
     * 将IP转为Long型
     *
     * @param ip
     * @return 转型后的long值
     */
    public static Long getLongFromIp(String ip) {

        if (!isEmpty(ip) && ip.matches(RegexConstant.IP_REG)) {

            ip = ip.trim();

            String[] ipArr = ip.split("\\.");

            long ip2long = 0L;

            for (int i = 0; i < 4; ++i) {

                ip2long = ip2long << 8 | Integer.parseInt(ipArr[i]);

            }

            return ip2long;

        }

        return 0L;
    }

    /**
     * 判定IP是否在指定IP段内
     *
     * @param ipScope1 IP段起始IP
     * @param ipScope2 IP段结束IP
     * @param ipTarget 目标IP
     * @return 判定结果，TRUE：在范围内，FALSE：不在范围内
     */
    public static boolean checkIp(String ipScope1, String ipScope2, String ipTarget) {

        if (!isEmpty(ipTarget)) {

            Long scope1 = getLongFromIp(ipScope1);
            Long scope2 = getLongFromIp(ipScope2);
            Long target = getLongFromIp(ipTarget);

            if (scope1 > 0 && scope2 > 0 && target > 0) {

                return (scope1 <= target && scope2 >= target) || (scope1 >= target && scope2 <= target);

            } else {

                return false;
            }

        }

        return false;
    }

    /**
     * 判断字符串是否匹配正则表达式
     *
     * @param strTarget
     * @param regex
     * @return
     */
    public static boolean checkRegexCompile(String strTarget, String regex) {

        if (!isEmpty(strTarget) && !isEmpty(regex)) {

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(strTarget);

            return matcher.find();
        }

        return false;
    }

    /**
     * 获取正则表达式匹配到的内容
     *
     * @param strTarget
     * @param regex
     * @return
     */
    public List<String> getRegexCompile(String strTarget, String regex, List<String> result) {

        if (result == null) {
            result = new ArrayList<String>();
        } else {
            result.clear();
        }

        if (!isEmpty(strTarget) && !isEmpty(regex)) {

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(strTarget);

            while (matcher.find()) {

                if (result == null) {
                    result = new ArrayList<>();
                }

                result.add(matcher.group());

            }
        }

        return result;
    }

}

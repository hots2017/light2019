package com.hots.common.component.string;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.hots.common.constant.CommonConstant;
import com.hots.common.constant.RegexConstant;
import com.hots.common.exception.BasicException;
import com.hots.common.util.StringUtil;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

public class ChineseComponent {

    public HanyuPinyinOutputFormat defaultFormat;

    {
        defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    /**
     * 获取首字母全拼(存在多音字，用组装的方式返回)
     *
     * @param chinese
     * @param split   不同汉字之间的分隔符号
     * @return
     */
    public Set<String> converterToFull(String chinese, String split) {

        Set<String> result = new HashSet<String>();

        List<Set<String>> pinyinNameList = converterToFullSpell(chinese);

        if (pinyinNameList != null && pinyinNameList.size() > 0) {

            List<String[]> fullSpell = new ArrayList<String[]>();

            Set<String> nameNew = null;
            for (Set<String> set : pinyinNameList) {
                nameNew = new HashSet<String>();
                for (String name : set) {
                    if (!StringUtil.isEmpty(name)) {
                        nameNew.add(name);
                    }
                }
                if (nameNew.size() > 0) {
                    fullSpell.add(StringUtil.collectionToStr(nameNew, ";").split(";"));
                }
            }

            // 第一个字部分
            List<String[]> resultBasic = new ArrayList<String[]>();
            String[] tmpArr = new String[fullSpell.size()];
            for (int i = 0; i < fullSpell.size(); i++) {
                tmpArr[i] = fullSpell.get(i)[0];
            }
            resultBasic.add(tmpArr);
            result.add(StringUtil.arrToStr(tmpArr, split));

            // 其他多音字部分
            for (int i = 0; i < fullSpell.size(); i++) {

                String[] nameFull = fullSpell.get(i);

                if (nameFull.length > 1) {
                    String[] nameNews = null;
                    for (int j = 1; j < nameFull.length; j++) {
                        String replace = nameFull[j];

                        for (String[] name : resultBasic) {
                            nameNews = name.clone();
                            nameNews[i] = replace;
                            result.add(StringUtil.arrToStr(nameNews, split));
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取首字母(存在多音字，用组装的方式返回)
     *
     * @param chinese
     * @param split   不同汉字之间的分隔符号
     * @return
     */
    public Set<String> converterToFirst(String chinese, String split) {
        Set<String> result = new HashSet<String>();
        Set<String> fullSpell = converterToFull(chinese, split);

        if (fullSpell != null && fullSpell.size() > 0) {
            for (String name : fullSpell) {
                String tmp = "";
                for (String nameSplit : name.split(split)) {
                    if (!StringUtil.isEmpty(nameSplit.trim())) {
                        tmp += nameSplit.trim().substring(0, 1);
                    }
                }
                result.add(tmp);
            }
        }

        return result;
    }

    /**
     * 获取中文字母拼写(包含多音字拼音)
     *
     * @param chinese 中文文字
     */
    public List<Set<String>> converterToFullSpell(String chinese) {
        List<Set<String>> pinyinNameList = new ArrayList<Set<String>>();

        Set<String> formatNameSet = null;

        // 不包含中文，则直接返回
        if (!StringUtil.checkRegexCompile(chinese, RegexConstant.CN_REG)) {
            formatNameSet = new HashSet<String>();
            formatNameSet.add(chinese);
            pinyinNameList.add(formatNameSet);
            return pinyinNameList;
        }

        String[] noCn = chinese.split(RegexConstant.CN_REG);
        char[] nameChar = chinese.replaceAll(RegexConstant.NO_CN_REG, CommonConstant.COM_SPLIT).toCharArray();

        for (int i = 0; i < nameChar.length; i++) {

            formatNameSet = new HashSet<String>();
            String ch = String.valueOf(nameChar[i]);

            if (ch.matches(RegexConstant.CN_REG)) {

                formatNameSet = getFullSpell(nameChar[i], formatNameSet);

            } else if (ch.equals(CommonConstant.COM_SPLIT) && noCn != null && noCn.length > i) {

                formatNameSet.add(noCn[i]);
            }

            if (formatNameSet.size() > 0) {
                pinyinNameList.add(formatNameSet);
            }
        }

        return pinyinNameList;
    }

    /**
     * 获取中文字母拼写
     */
    private Set<String> getFullSpell(char chinese, Set<String> result) {
        if (result == null) {
            result = new HashSet<>();
        } else {
            result.clear();
        }

        try {
            String[] tmpArr = PinyinHelper.toHanyuPinyinStringArray(chinese, defaultFormat);

            if (tmpArr != null && tmpArr.length > 0) {
                for (int i = 0; i < tmpArr.length; i++) {
                    if (!result.contains(tmpArr[i])) {
                        result.add(tmpArr[i]);
                    }
                }
            }

        } catch (Exception e) {

            new BasicException("获取中文字母拼写错误", chinese, e);

        }

        return result;

    }
}

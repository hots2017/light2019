package com.hots.common.component.string;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hots.common.exception.BasicException;
import com.hots.common.util.StringUtil;
import com.hots.common.util.TimeUtil;

@Component
public class JsonComponent{

    public ObjectMapper objectMapper = new ObjectMapper();

    {

        // 转换为格式化的json
        // objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // 序列化的时候序列对象的所有属性
        // Include.ALWAYS 是序列化对像所有属性
        // Include.NON_NULL 只有不为null的字段才被序列化
        // Include.NON_EMPTY 如果为null或者 空字符串和空集合都不会被序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        // 反序列化的时候如果多了其他属性(get方法),不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 如果是空对象的时候,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat((TimeUtil.FORMAT_TIME_EN)));

    }

    /**
     * Bean转JSON
     *
     * @param obj
     * @param     <T>
     * @return
     */
    public <T> String obj2Json(T obj) {

        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            new BasicException("对象转JSON失败", obj, e);
        }

        return null;
    }

    /**
     * Bean Json转Bean对象
     *
     * @param jsonStr
     * @param type
     * @param         <T>
     * @return
     */

    public <T> T getBeanFromJsonByType(String jsonStr, TypeReference<?> type) {

        if (!StringUtil.isEmpty(jsonStr)) {
            try {
                return objectMapper.readValue(jsonStr, type);
            } catch (Exception e) {
                new BasicException("JSON解析失败", jsonStr, e);
            }
        }
        return null;
    }
}

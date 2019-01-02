package com.hots.common.component.file;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.hots.common.exception.BasicException;
import com.hots.common.util.StreamUtil;
import com.hots.common.util.StringUtil;

@Component
public class PropertiesFileComponent {

    /**
     * 获取Properties文件的所有参数
     * 
     * @param location
     * @return
     */
    public Map<String, String> getPropertyAll(String location) {
        Map<String, String> resultMap = new HashMap<String, String>();

        FileReader reader = null;
        try {
            reader = new FileReader(new File(location));
            Properties properties = new Properties();
            properties.load(reader);

            Set<String> result = properties.stringPropertyNames();
            if (result != null && result.size() > 0) {
                for (String key : result) {
                    if (!StringUtil.isEmpty(key)) {
                        resultMap.put(key.trim(),
                                properties.getProperty(key) != null ? properties.getProperty(key).trim() : "");
                    }
                }
            }
        } catch (Exception e) {
            new BasicException("Properties文件信息获取失败", location, e);
        } finally {
            StreamUtil.closeReader(reader);
        }

        return resultMap;
    }

    /**
     * 获取Properties文件的指定参数
     * 
     * @param location
     * @param key
     * @return
     */
    public String getProperty(String location, String key) {

        FileReader reader = null;
        try {
            reader = new FileReader(new File(location));
            Properties properties = new Properties();
            properties.load(reader);

            return properties.getProperty(key) != null ? properties.getProperty(key).trim() : null;
        } catch (Exception e) {
            new BasicException("Properties文件信息获取失败", location + ":" + key, e);
        } finally {
            StreamUtil.closeReader(reader);
        }

        return null;
    }

}

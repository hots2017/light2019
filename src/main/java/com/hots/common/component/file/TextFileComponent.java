package com.hots.common.component.file;

import java.io.FileWriter;

import org.springframework.stereotype.Component;

import com.hots.common.exception.BasicException;
import com.hots.common.util.StreamUtil;

@Component
public class TextFileComponent {

    /**
     * 保存文本内容到文件
     *
     * @param filePath 文件地址
     * @param str      文本内容
     * @param append   追加或是覆盖
     */
    public void writeStrToFile(String filePath, String str, boolean append) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath, append);
            if (append) {
                fileWriter.write(System.getProperty("line.separator"));
            }
            fileWriter.write(str);
        } catch (Exception e) {
            new BasicException("保存文本内容到本地文件错误", "文件路径：" + filePath + "; 文件内容：" + str, e);
        } finally {
            StreamUtil.closeWriter(fileWriter);
        }
    }
}

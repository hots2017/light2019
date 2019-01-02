package com.hots.common.component.excel.basic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

public class BasicExcelExport{

    /**
     * 递归调用，得到实体类所有通过注解映射了数据表的字段
     *
     * @param clazz
     * @param fieldsResult 结果集
     * @param <T>
     * @return
     */
    protected <T> List<Field> getMappedFiled(Class<T> clazz, List<Field> fieldsResult) {
        if (fieldsResult == null) {
            fieldsResult = new ArrayList<Field>();
        }

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(BasicCellField.class)) {
                fieldsResult.add(field);
            }
        }

        return fieldsResult;
    }

    /**
     * 单元格属性
     */
    protected CellStyle getCellStyleComm(Workbook workbook) {
        // 设置样式;
        CellStyle style = workbook.createCellStyle();
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置字体
        Font font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 12);
        // 字体加粗
        font.setBold(false);
        // 设置字体名字
        font.setFontName("Courier New");
        style.setFont(font);

        return style;
    }

    /**
     * 单元格属性
     */
    protected CellStyle getCellStyleHead(Workbook workbook) {
        // 设置样式;
        CellStyle style = workbook.createCellStyle();
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置字体
        Font font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 12);
        // 字体加粗
        font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        style.setFont(font);

        return style;
    }

}

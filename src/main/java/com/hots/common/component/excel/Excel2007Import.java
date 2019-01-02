package com.hots.common.component.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 * Excel2007导入
 *
 * @param <T>
 */
@Component
public class Excel2007Import<T> {

    private Class<T> handler;

    private XSSFWorkbook workbook;

    {
        this.setWorkbook(new XSSFWorkbook());
    }

    public Excel2007Import() {

    }

    public Excel2007Import(Class<T> handler) {
        this.setHandler(handler);
    }

    public XSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public Class<T> getHandler() {
        return handler;
    }

    public void setHandler(Class<T> handler) {
        this.handler = handler;
    }

}

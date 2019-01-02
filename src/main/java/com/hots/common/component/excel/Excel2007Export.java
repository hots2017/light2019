package com.hots.common.component.excel;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hots.common.component.excel.basic.BasicCellField;
import com.hots.common.component.excel.basic.BasicExcelExport;
import com.hots.common.component.file.FileSystemComponent;
import com.hots.common.exception.BasicException;
import com.hots.common.util.StreamUtil;
import com.hots.common.util.StringUtil;

/**
 * 用于导出大数据量Excel 文件类型xlsx (1048576行，16384列)
 *
 * @param <T>
 */
@Component
public class Excel2007Export<T> extends BasicExcelExport {

    @Autowired
    private FileSystemComponent fileSystemComponent;

    private Class<T> handler;

    private SXSSFWorkbook workbook;

    {
        this.workbook = new SXSSFWorkbook(1000);
    }

    public void initHadler(Class<T> handler) {
        this.handler = handler;
    }

    /**
     * 下载模板文件（类名做为文件名）
     *
     * @param response
     * @param fileName 文件名称
     * @param          <T>
     */
    public void exportExcelPrototype(HttpServletResponse response, String fileName) {

        try {
            if (StringUtil.isEmpty(fileName)) {
                fileName = handler.getSimpleName();
            }

            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");

            createSheet(0, handler.getSimpleName(), null);

            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
            response.setContentType("application/msexcel");

            workbook.write(response.getOutputStream());
            workbook.dispose();
        } catch (Exception e) {
            new BasicException("Excel导出失败", null, e);
        }
    }

    /**
     * @param fileName
     * @param paramData
     * @param response
     * @param           <T>
     */
    public void exportExcel(String fileName, Map<String, List<T>> paramData, HttpServletResponse response) {

        try {
            fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");

            if (paramData != null && paramData.size() > 0) {
                int index = 0;
                for (String sheetName : paramData.keySet()) {
                    createSheet(index++, sheetName, paramData.get(sheetName));
                }
            }

            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
            response.setContentType("application/msexcel");

            workbook.write(response.getOutputStream());
            workbook.dispose();
        } catch (Exception e) {
            new BasicException("Excel导出失败", null, e);
        }
    }

    /**
     * 生成Excel
     *
     * @param paramData 数据源，key值为工作表的名称
     * @param location  存储路径
     * @param           <T>
     */
    public void createBook(Map<String, List<T>> paramData, String location) {
        OutputStream outputStream = null;
        try {
            // 输出路径
            outputStream = new FileOutputStream(fileSystemComponent.createFile(location, true));

            if (paramData != null && paramData.size() > 0) {
                int index = 0;
                // 追加工作表
                for (String sheetName : paramData.keySet()) {
                    createSheet(index++, sheetName, paramData.get(sheetName));
                }
            }

            // 生成文件
            try {
                if (workbook != null) {
                    outputStream.flush();
                    workbook.write(outputStream);
                    workbook.dispose();
                }
            } catch (Exception e) {
                new BasicException("Excel导出失败", null, e);
            } finally {
                StreamUtil.closeOutputStream(outputStream);
            }

        } catch (Exception e) {
            new BasicException("Excel导出失败。", null, e);
        }
    }

    /**
     * 生成Excel：元数据导出到指定工作表中
     *
     * @param index     工作表序号
     * @param sheetName 工作表名称
     * @param beanList  源数据
     * @param           <T>
     */
    public void createSheet(int index, String sheetName, List<T> beanList) {

        // 1.创建工作表
        Sheet sheet = workbook.createSheet();
        workbook.setSheetName(index, sheetName);

        // 2.获取实体类标有@BasicCellField注解的Field对象（基础类型）
        List<Field> fields = getMappedFiled(handler, null);

        // 正文单元格样式
        CellStyle cellStyleHead = getCellStyleHead(workbook);

        // 3. 标题列
        BasicCellField[] attrArr = new BasicCellField[fields.size()];

        Row row = sheet.createRow(0);
        Cell cell = null;

        for (int i = 0; i < fields.size(); i++) {
            // 注解信息
            attrArr[i] = fields.get(i).getAnnotation(BasicCellField.class);
            // 创建列
            cell = row.createCell(attrArr[i].column());
            // 写入列名
            cell.setCellValue(attrArr[i].name());
            // 单元格属性
            cell.setCellStyle(cellStyleHead);
            // 数据类型
            cell.setCellType(CellType.STRING);
        }

        // 4. 导出内容
        if (beanList != null && beanList.size() > 0) {
            Object obj = null;
            for (int i = 1; i <= beanList.size(); i++) {

                row = sheet.createRow(i);

                T bean = (T) beanList.get(i - 1);

                for (int j = 0; j < fields.size(); j++) {
                    try {

                        Field field = fields.get(j);
                        field.setAccessible(true);

                        BasicCellField attr = attrArr[j];

                        if (attr.isExport()) {
                            cell = row.createCell(attr.column());

                            obj = field.get(bean);

                            // 数据类型（设定为字符串类型，可改为其他形式的数值）
                            cell.setCellType(CellType.STRING);

                            // 单元格属性
                            cell.setCellStyle(cellStyleHead);

                            if (obj == null) {
                                cell.setCellValue("");
                            } else {
                                cell.setCellValue(String.valueOf(obj));
                            }
                        }
                    } catch (Exception e) {
                        new BasicException("数据保存到单元格失败(", i + "行" + j + "列)" + "：" + String.valueOf(obj), e);
                    }
                }
            }
        }
    }
}

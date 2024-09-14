package org.lyflexi.customeasyexcelframework.commonApi.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.lyflexi.customeasyexcelframework.commonApi.enums.ExportClassEnum;
import org.lyflexi.customeasyexcelframework.commonApi.strategy.ExcelFillCellRowMergeStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 17:18
 */
@Slf4j
@Component
public class ExcelExportUtil<T, E> {

    /**
     * 多个子页签导出功能
     *
     * @param response  响应流
     * @param fileName  导出的文件名
     * @param data      导出的数据
     * @param classArgs 导出的页签EXCEL类
     * @throws Exception
     */
    public void exportMultipleSheetData(HttpServletResponse response, String fileName, Map<Class, List<? extends Object>> data, Class<E>... classArgs) throws IOException {
        this.exportMergeMultipleSheetData(response, fileName, null, 0, data, classArgs);
    }

    /**
     * 多个子页签导出并且合并功能
     *
     * @param response         响应流
     * @param fileName         导出的文件名
     * @param mergeColumnIndex 需要合并的列
     * @param mergeRowIndex    设置第几行开始合并
     * @param data             导出的数据
     * @param classArgs        导出的页签EXCEL类
     * @throws Exception
     */
    public void exportMergeMultipleSheetData(HttpServletResponse response, String fileName, int[] mergeColumnIndex, int mergeRowIndex,
                                             Map<Class, List<? extends Object>> data, Class<E>... classArgs) throws IOException {
        try {
            fileName = new String(fileName.getBytes(), "UTF-8");
            log.info("导出的文件名为：" + fileName);
            response.addHeader("Content-Disposition", "filename=" + fileName);
            //设置类型，扩展名为.xls
            response.setContentType("application/vnd.ms-excel");
            //将数据写入sheet页中
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();

            // 遍历每个页签实体
            for (int i = 0; i < classArgs.length; i++) {
                // 判断数据中是否存在实体类，如果存在则做导出
                if (data.containsKey(classArgs[i])) {
                    // Excel单元格行合并处理策略
                    ExcelFillCellRowMergeStrategy rowMergeStrategy = null;
                    WriteSheet writeSheet = null;
                    if (null != mergeColumnIndex) {
                        rowMergeStrategy = new ExcelFillCellRowMergeStrategy(mergeRowIndex, mergeColumnIndex);
                        writeSheet = EasyExcel.writerSheet(i, ExportClassEnum.getValue(String.valueOf(classArgs[i]))).registerWriteHandler(rowMergeStrategy).head(classArgs[i]).build();
                    }else {
                        //若自定义rowMergeStrategy==null,则writerSheet方法不能调用registerWriteHandler，否则新版easyexcel-3.1.2会报空指针异常
                        writeSheet = EasyExcel.writerSheet(i, ExportClassEnum.getValue(String.valueOf(classArgs[i]))).head(classArgs[i]).build();
                    }
                    excelWriter.write(data.get(classArgs[i]), writeSheet);
                }
            }
            excelWriter.finish();
            log.info("导出文件《" + fileName + "》成功");
            response.flushBuffer();
        } catch (Exception e) {
            log.error("导出失败", e);
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    /**
     * 动态导出列
     * @param response
     * @param fileName
     * @param headList:用于导出模板表的时候，设置表头
     * @param dataList
     */
    public void exportData(HttpServletResponse response, String fileName, List<List<String>> headList, List<List<Object>> dataList) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        response.addHeader("Access-Control-Expose-Headers", "Content-disposition");
        //表头样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置表头居中对齐
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //内容样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置内容靠左对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        try {
            EasyExcel.write(response.getOutputStream()).head(headList).inMemory(true)
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .registerWriteHandler(new AbstractColumnWidthStyleStrategy() {
                        @Override
                        protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
                            // 简单设置
                            Sheet sheet = writeSheetHolder.getSheet();
                            sheet.setColumnWidth(cell.getColumnIndex(), 5000);
                        }
                    })
                    .autoCloseStream(Boolean.FALSE).sheet(fileName).doWrite(dataList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

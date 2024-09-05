package org.lyflexi.customeasyexcelframework.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.customeasyexcelframework.enums.ExcelEnum;
import org.lyflexi.customeasyexcelframework.resolver.ExcelResolver;
import org.lyflexi.customeasyexcelframework.result.Result;
import org.lyflexi.customeasyexcelframework.service.IExcelService;
import org.lyflexi.customeasyexcelframework.utils.ExcelExportUtil;
import org.lyflexi.customeasyexcelframework.utils.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 16:57
 */

@Slf4j
@Component
public class ExcelServiceImpl implements IExcelService {

    @Autowired
    private ExcelExportUtil<Object, Object> excelExportUtil;

    @Autowired
    private ExcelImportUtil<Object, Object> excelImportUtil;

    @Override
    public Result<String> exportExcel(HttpServletResponse response, String type, HashMap<String, Object> param) {
        ExcelEnum exportEnum = ExcelEnum.match(type);
        if (ExcelEnum.NONE.equals(exportEnum)) {
            return Result.fail("不支持的导入类型：" + type);
        }
        Class<? extends ExcelResolver> resolver = exportEnum.getResolverClazz();
        ExcelResolver exportResolver;
        try {
            exportResolver = resolver.newInstance();
            return exportResolver.exportData(response, param, exportEnum.getEntityClazz(), excelExportUtil);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("type", e);
        }
        return Result.fail("创建导出解析实例失败");
    }

    @Override
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, String type, MultipartFile file) {
        ExcelEnum importEnum = ExcelEnum.match(type);
        if (ExcelEnum.NONE.equals(importEnum)) {
            return Result.fail("不支持的导入类型：" + type);
        }
        Class<? extends ExcelResolver> resolver = importEnum.getResolverClazz();
        ExcelResolver importResolver;
        try {
            importResolver = resolver.newInstance();
            return importResolver.importData(request, response, file, importEnum.getEntityClazz(), excelImportUtil);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("type", e);
        }
        return Result.fail("创建导入解析实例失败");
    }
}

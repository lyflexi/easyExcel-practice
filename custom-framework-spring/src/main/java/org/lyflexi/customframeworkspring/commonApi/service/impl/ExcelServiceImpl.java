package org.lyflexi.customframeworkspring.commonApi.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.customframeworkspring.commonApi.enums.ExcelEnum;
import org.lyflexi.customframeworkspring.commonApi.resolver.ExcelResolver;
import org.lyflexi.customframeworkspring.commonApi.result.Result;
import org.lyflexi.customframeworkspring.commonApi.service.IExcelService;
import org.lyflexi.customframeworkspring.commonApi.utils.ExcelExportUtil;
import org.lyflexi.customframeworkspring.commonApi.utils.ExcelImportUtil;
import org.lyflexi.customframeworkspring.commonApi.utils.SpringUtils;
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
        ExcelResolver exportResolver;

        try {
            exportResolver = SpringUtils.getBean(exportEnum.GetResolverName(), ExcelResolver.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return exportResolver.exportData(response, param, exportEnum.getEntityClazz(), excelExportUtil);
    }

    @Override
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, String type, MultipartFile file) {
        ExcelEnum importEnum = ExcelEnum.match(type);
        if (ExcelEnum.NONE.equals(importEnum)) {
            return Result.fail("不支持的导入类型：" + type);
        }
        ExcelResolver importResolver;
        try {
            importResolver = SpringUtils.getBean(importEnum.GetResolverName(), ExcelResolver.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return importResolver.importData(request, response, file, importEnum.getEntityClazz(), excelImportUtil);
    }
}

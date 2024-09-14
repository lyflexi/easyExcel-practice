package org.lyflexi.customframeworkspring.commonApi.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.lyflexi.customframeworkspring.commonApi.result.Result;
import org.lyflexi.customframeworkspring.commonApi.utils.ExcelExportUtil;
import org.lyflexi.customframeworkspring.commonApi.utils.ExcelImportUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 17:10
 */
public interface ExcelResolver<E> {
    /**
     * 导入数据
     * @param request
     * @param response
     * @param file
     * @param clazz
     * @param excelImportUtil
     * @return
     */
    Result<?> importData(HttpServletRequest request, HttpServletResponse response, MultipartFile file,
                         Class<E> clazz, ExcelImportUtil<E, E> excelImportUtil);
    /**
     * excel导出数据
     * @param param
     * @param clazz
     * @param response
     * @param excelImportUtil
     */
    Result<String> exportData(HttpServletResponse response, HashMap<String, Object> param, Class<E> clazz,
                              ExcelExportUtil<E, E> excelImportUtil);
}

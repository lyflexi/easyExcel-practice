package org.lyflexi.customeasyexcelframework.commonApi.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.lyflexi.customeasyexcelframework.commonApi.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 16:49
 */
public interface IExcelService {
    Result<String> exportExcel(HttpServletResponse response, String type, HashMap<String, Object> param);

    /**
     * 导入各类excel数据记录
     *
     * @param type
     * @param request
     * @param response
     * @param file
     * @return 返回值 result泛型支持两种类型：
     * 1）String 表示导入过程出现阻塞，result.msg为有效异常信息；
     * 2）List<ExcelErrorResultVo> 表示存在单行数据出现异常，数组result.data为有效异常信息列表
     */
    Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, String type, MultipartFile file);
}

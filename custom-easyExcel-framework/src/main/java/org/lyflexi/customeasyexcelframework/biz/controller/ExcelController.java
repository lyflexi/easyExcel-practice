package org.lyflexi.customeasyexcelframework.biz.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.customeasyexcelframework.commonApi.result.Result;
import org.lyflexi.customeasyexcelframework.commonApi.service.IExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 16:46
 */
@RestController
@RequestMapping("/excel")
@Slf4j
public class ExcelController {
    @Autowired
    private IExcelService excelService;

    @PostMapping(value = "/readExcel")
    public Result<?> importData(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "file", required = true) MultipartFile file,
                                @RequestParam(value = "type", required = true) String type) throws IOException {
        return excelService.importExcel(request, response, type, file);
    }

    @PostMapping(value = "/exportExcel")
    public void exportExcel(HttpServletResponse response,
                            @RequestBody HashMap<String, Object> param,
                            @RequestParam(value = "type", required = true) String type) throws Exception {
        excelService.exportExcel(response, type, param);
    }
}


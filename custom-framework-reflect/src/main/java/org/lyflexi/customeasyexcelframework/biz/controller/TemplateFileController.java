package org.lyflexi.customeasyexcelframework.biz.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.customeasyexcelframework.commonApi.entity.TemplateDownloadParam;
import org.lyflexi.customeasyexcelframework.commonApi.utils.TemplateFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/5 9:52
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class TemplateFileController {


    @Autowired
    TemplateFileUtil templateFileUtil;

    /**
     * 示例：{"templateName":"baseFactoryCode"}
     * {"templateName":"pullDeliveryPath"}
     * @param templateDownloadParam
     * @param response
     * @return
     */
    @PostMapping("/downloadTemplate")
    public void download(@RequestBody TemplateDownloadParam templateDownloadParam, HttpServletResponse response) {
        templateFileUtil.download(templateDownloadParam,response);
    }

}

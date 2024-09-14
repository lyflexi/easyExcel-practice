package org.lyflexi.customframeworkspring.commonApi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/5 9:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TemplateDownloadParam {
    /**
     * 下载文件的模板名称
     */
    private String templateName;
}
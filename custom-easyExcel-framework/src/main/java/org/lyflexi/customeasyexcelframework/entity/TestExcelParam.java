package org.lyflexi.customeasyexcelframework.entity;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/5 8:54
 */

@Data
public class TestExcelParam {


    private String productMaterialCode;

    private String productLineCode;

    private String operationBomCode;

    private String enableStatus;

    private List<String> ids;
}
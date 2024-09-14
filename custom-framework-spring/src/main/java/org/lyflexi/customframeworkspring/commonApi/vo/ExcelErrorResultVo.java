package org.lyflexi.customframeworkspring.commonApi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 17:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelErrorResultVo {

    private String sheet;
    private Integer index;
    private String code;
    private String name;
    private String operateType;
    private String errorInfo;

    public ExcelErrorResultVo(Integer index, String errorInfo) {
        this.index = index;
        this.errorInfo = errorInfo;
    }

    public ExcelErrorResultVo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public ExcelErrorResultVo(Integer index, String code, String name, String errorInfo) {
        this.index = index;
        this.code = code;
        this.name = name;
        this.errorInfo = errorInfo;
    }

    public ExcelErrorResultVo(Integer index, String code, String name, String operateType, String errorInfo) {
        this.index = index;
        this.code = code;
        this.name = name;
        this.operateType = operateType;
        this.errorInfo = errorInfo;
    }

    public ExcelErrorResultVo(String sheet, Integer index, String code, String name, String errorInfo) {
        this.sheet = sheet;
        this.index = index;
        this.code = code;
        this.name = name;
        this.errorInfo = errorInfo;
    }
}


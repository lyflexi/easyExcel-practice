package org.lyflexi.customeasyexcelframework.commonApi.exception;

import lombok.Getter;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/13 13:38
 */

@Getter
public enum LyflexiErrorType implements ErrorType {
    INTERNET_SERVER_ERROR("000001", "服务器内部错误"),
    IMPORT_TEMPLATE_ERROR("000002", "导入错误"),
    /*template下载信息*/
    SERVICE_RESOURCE_NOT_EXIST("050002", "req.source.not.exist"),
    INCOMPLETE_PARAM("050006", "incomplete.param"),
    ;
    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String mesg;

    LyflexiErrorType(String code, String mesg) {
        this.code = code;
        this.mesg = mesg;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMesg() {
        return mesg;
    }

    public void setMesg(String mesg) {
        this.mesg = mesg;
    }
}

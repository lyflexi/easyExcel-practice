package org.lyflexi.customeasyexcelframework.aware;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 17:04
 */

public interface IExcelRowInfoAware {
    /**
     * 获取excel行记录的名称，一般使用第一列即可
     * @return
     */
    String getRecordName();

    /**
     * 构造数据记录全局唯一的主键key，用于导入数据去重校验
     * （1）可使用该数据的全局唯一编码或者主键id
     * （2）若无全局唯一的字段，可以使用字段组合成全局唯一的字符串
     * @param
     * @return
     */
    String getUniqueKey();
}

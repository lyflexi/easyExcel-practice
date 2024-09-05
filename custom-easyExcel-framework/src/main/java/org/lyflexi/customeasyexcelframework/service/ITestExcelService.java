package org.lyflexi.customeasyexcelframework.service;

import org.lyflexi.customeasyexcelframework.entity.TestExcelEntity;
import org.lyflexi.customeasyexcelframework.entity.TestExcelParam;

import java.util.List;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/5 8:56
 */
public interface ITestExcelService {
    List<TestExcelEntity> listFlatEntity(TestExcelParam param);
}

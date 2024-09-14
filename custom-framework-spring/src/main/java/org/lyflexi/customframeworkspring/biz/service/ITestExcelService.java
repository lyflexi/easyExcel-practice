package org.lyflexi.customframeworkspring.biz.service;


import org.lyflexi.customframeworkspring.commonApi.entity.TestExcelEntity;
import org.lyflexi.customframeworkspring.commonApi.entity.TestExcelParam;

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

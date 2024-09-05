package org.lyflexi.customeasyexcelframework.service.impl;

import org.lyflexi.customeasyexcelframework.entity.TestExcelEntity;
import org.lyflexi.customeasyexcelframework.entity.TestExcelParam;
import org.lyflexi.customeasyexcelframework.service.ITestExcelService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/5 8:57
 */
@Service
public class TestExcelServiceImpl implements ITestExcelService {
    @Override
    public List<TestExcelEntity> listFlatEntity(TestExcelParam param) {

        ArrayList<TestExcelEntity> ans = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            TestExcelEntity testExcelEntity = new TestExcelEntity();
            testExcelEntity.setProductMaterialCode("testExcelEntity");
            testExcelEntity.setProductMaterialName("testExcelEntity");
            testExcelEntity.setProductMaterialVersion("testExcelEntity");
            testExcelEntity.setLineCode("testExcelEntity");
            testExcelEntity.setLineName("testExcelEntity");
            testExcelEntity.setOperationBomCode("testExcelEntity");
            testExcelEntity.setOperationBomName("testExcelEntity");
            testExcelEntity.setOperationBomVersion("testExcelEntity");
            testExcelEntity.setRouteCode("testExcelEntity");
            testExcelEntity.setRouteName("testExcelEntity");
            testExcelEntity.setRouteVersion("testExcelEntity");
            testExcelEntity.setEnableSapBomVersion(0);
            testExcelEntity.setLineBeat(BigDecimal.ONE);
            testExcelEntity.setEnableStatus(0);
            ans.add(testExcelEntity);
        }
        return ans;
    }
}

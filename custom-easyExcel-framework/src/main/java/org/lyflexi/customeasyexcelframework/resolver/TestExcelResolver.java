package org.lyflexi.customeasyexcelframework.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.customeasyexcelframework.entity.TestExcelEntity;
import org.lyflexi.customeasyexcelframework.entity.TestExcelParam;
import org.lyflexi.customeasyexcelframework.service.ITestExcelService;
import org.lyflexi.customeasyexcelframework.utils.SpringUtils;
import org.lyflexi.customeasyexcelframework.vo.ExcelErrorResultVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 17:28
 */
@Slf4j
public class TestExcelResolver extends AbstractExcelResolver<TestExcelEntity>{

    private final ITestExcelService iTestExcelService;
//    private final BaseMaterialService baseMaterialService;
//    private final BaseModelService baseModelService;
//    private final IBaseOperationBomService iBaseOperationBomService;

    public static final String SPLITTER =  "[:,]+";


    public TestExcelResolver() {
        iTestExcelService = SpringUtils.getBean(ITestExcelService.class);
//        baseMaterialService = SpringUtils.getBean(BaseMaterialService.class);
//        baseModelService = SpringUtils.getBean(BaseModelService.class);
//        iBaseOperationBomService = SpringUtils.getBean(IBaseOperationBomService.class);
    }

    @Override
    protected List<TestExcelEntity> retriveDataForExport(Map<String, Object> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        TestExcelParam param = objectMapper.convertValue(map, TestExcelParam.class);
        return iTestExcelService.listFlatEntity(param);
    }

    @Override
    protected List<ExcelErrorResultVo> convertDataForImport(List<TestExcelEntity> excelList, List<TestExcelEntity> toSaveList) {
        List<ExcelErrorResultVo> errorList = new ArrayList<>();
        int index = 0;
        //对每组产品下的输入进行校验
        Map<String, List<TestExcelEntity>> prodToList = excelList.stream().collect(Collectors.groupingBy(TestExcelEntity::getRecordName));
        for (Map.Entry<String, List<TestExcelEntity>> entry : prodToList.entrySet()){
            String key = entry.getKey();
            List<TestExcelEntity> value = entry.getValue();
            //用于构建ExcelErrorResultVo
            TestExcelEntity entity = excelList.get(index);
            //校验产品必须是有效产品
            String[] split = key.split(SPLITTER);
            String productMaterialCode = split[1];
            String productMaterialVersion = split[3];
//            BaseMaterialPo curProd = baseMaterialService.getByCodeAndVersion(productMaterialCode, productMaterialVersion);
//            if (Objects.isNull(curProd)){
//                ExcelErrorResultVo error = new ExcelErrorResultVo((index + 1), entity.getUniqueKey(),
//                        entity.getRecordName(), "该产品物料不存在：" + key);
//                errorList.add(error);
//                continue;
//            }
//
//            //校验产线必须是有效产线
//            List<String> curLineCodes = value.stream().map(TestExcelEntity::getLineCode).distinct().collect(Collectors.toList());
//            for (String curLine:curLineCodes){
//                BaseModelPo modelPo = baseModelService.getOne(Wrappers.<BaseModelPo>lambdaQuery()
//                        .eq(BaseModelPo::getModelTypeCode, ModelTypeEnum.PRODUCT_LINE.getCode())
//                        .eq(BaseModelPo::getModelCode, curLine)
//                        .eq(BaseModelPo::getDataStatus, DataStatusEnum.NORMAL.getCode()));
//                if (Objects.isNull(modelPo)){
//                    ExcelErrorResultVo error = new ExcelErrorResultVo((index + 1), entity.getUniqueKey(),
//                            entity.getRecordName(), "该产品物料："+key+",对应的产线："+curLine+"不存在");
//                    errorList.add(error);
//                }
//            }
//            //校验工艺Bom必须是有效工艺Bom
//            List<String> curOperationBomCodes = value.stream().map(TestExcelEntity::getOperationBomCode).distinct().collect(Collectors.toList());
//            List<BaseOperationBomPo> operationBomPos= iBaseOperationBomService.list(Wrappers.<BaseOperationBomPo>lambdaQuery()
//                    .eq(BaseOperationBomPo::getDataStatus, DataStatusEnum.NORMAL.getCode())
//                    .eq(BaseOperationBomPo::getProductMaterialCode, productMaterialCode)
//                    .eq(BaseOperationBomPo::getProductMaterialVersion, productMaterialVersion)
//            );
//            List<String> curPoOperationBomCodes = operationBomPos.stream().map(BaseOperationBomPo::getOperationBomCode).distinct().collect(Collectors.toList());
//            curOperationBomCodes.forEach(curOperationBomCode -> {
//                if (!curPoOperationBomCodes.contains(curOperationBomCode)){
//                    ExcelErrorResultVo error = new ExcelErrorResultVo((index + 1), entity.getUniqueKey(),
//                            entity.getRecordName(), "该产品物料："+key+",对应的工艺Bom："+curOperationBomCode+"不存在");
//                    errorList.add(error);
//                }
//            });
//            //校验工艺路线必须是有效工艺路线
//            List<String> curRouteCodes = value.stream().map(TestExcelEntity::getRouteCode).distinct().collect(Collectors.toList());
//            List<String> curPoRouteCodes = operationBomPos.stream().map(BaseOperationBomPo::getRouteCode).distinct().collect(Collectors.toList());
//            curRouteCodes.forEach(curRouteCode -> {
//                if (!curPoRouteCodes.contains(curRouteCode)){
//                    ExcelErrorResultVo error = new ExcelErrorResultVo((index + 1), entity.getUniqueKey(),
//                            entity.getRecordName(), "该产品物料："+key+",对应的工艺路线："+curRouteCode+"不存在");
//                    errorList.add(error);
//                }
//            });
//            //表单校验，相同的产品不能选择重复的产线
//            if (curLineCodes.size() != value.size()) {
//                ExcelErrorResultVo error = new ExcelErrorResultVo((index + 1), entity.getUniqueKey(),
//                        entity.getRecordName(), "产品物料："+key+",不能导入重复的产线");
//                errorList.add(error);
//            }
//
//            //该产品不能与已有的绑定产线重复
//            List<BaseProdLineOperationBomPo> curPos = iBaseProdLineOperationBomService.list(Wrappers.<BaseProdLineOperationBomPo>lambdaQuery()
//                    .eq(BaseProdLineOperationBomPo::getDataStatus, DataStatusEnum.NORMAL.getCode())
//                    .eq(BaseProdLineOperationBomPo::getProductMaterialId, curProd.getId()));
//            if (!curPos.isEmpty()){
//                List<String> linesExisting = curPos.stream().map(BaseProdLineOperationBomPo::getLineCode).collect(Collectors.toList());
//                linesExisting.forEach(i->{
//                    if (curLineCodes.contains(i)){
//                        ExcelErrorResultVo error = new ExcelErrorResultVo((index + 1), entity.getUniqueKey(),
//                                entity.getRecordName(), "该产品物料："+key+",已经绑定过产线："+i+", 不能重复绑定产线");
//                        errorList.add(error);
//                    }
//                });
//            }
        }
        return errorList;
    }

    @Override
    protected void saveAllDataForImport(List<TestExcelEntity> toSaveList) {
        log.info("excel入库成功");
    }
}

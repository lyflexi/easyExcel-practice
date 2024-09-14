package org.lyflexi.customframeworkspring.commonApi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 17:22
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExportClassEnum {
    EXCELEXPORTOPERATIONENTITY("ExcelExportOperationEntity","1ExcelExportOperationEntity"),
    EXCELEXPORTOPERATIONENTITY2("ExcelExportOperationEntity2","2ExcelExportOperationEntity"),

    ORDERWIPREPORTENTITY("OrderWipReportEntity", "工单WIP"),
    SNDETAILINPUTENTITY("SnDetailInputEntity", "投入数"),
    SNDETAILWIPENTITY("SnDetailWipEntity", "WIP数"),
    SNDETAILBADENTITY("SnDetailBadEntity","不良数"),
    SNDETAILSCRAPENTITY("SnDetailScrapEntity","报废数"),
    SNDETAILPRODUCEENTITY("SnDetailProduceEntity","产出数"),
    MATERIALSTOCKSTREAMRECORDENTITY("MaterialStockStreamRecordEntity","物料出入流水"),
    PROUNORPACKINGENTITY("ProPackageEntity","装箱记录"),
    PROPACKAGEENTITY("ProPackageEntity","包装查询"),
    BACKTRACKENTITY("BacktrackEntity","反向追溯"),
    SHOPORDERWORKREPORTINGRECORDENTITY("ShopOrderWorkReportingRecordEntity","工单报工记录"),
    PRODUCTREPORTENTITY("ProductReportEntity","产品产量报表"),
    PROSNLIFECYCLEENTITY("ProSnLifeCycleEntity","SN生命周期"),
    STOCKINBILLENTITY("StockInBillEntity","入库信息"),
    CURRENTSTEPENTITY("CurrentStepEntity","当前步骤"),
    REPAIRRECORDENTITY("RepairRecordEntity","维修记录"),
    BADRECORDENTITY("BadRecordEntity","不良记录"),
    CHECKRECORDENTITY("CheckRecordEntity","检测记录"),
    CRITICALMATERIALENTITY("CriticalMaterialEntity","关键物料"),
    BOMENTITY("BomEntity","BOM"),
    STATIONRECORDENTITY("StationRecordEntity","过站记录"),
    PROPARAMETERDEVICEDATAENTITY("ProParameterDevicedataEntity","设备数采"),
    CXQUALITYTESTENTITY("CxQualityTestEntity","质量测试"),
    BASECONSISTENCYDETAILENTITY("BaseConsistencyDetailEntity","一致性明细记录"),
    OEEREPORTFORMSENTITY("OeeReportFormsEntity","OEE明细记录"),
    OEEREPORTFORMSALLENTITY("OeeReportFormsAllEntity","OEE合计"),
    OEEREPORTFORMSBYRATEENTITY("OeeReportFormsByRateEntity","OEE折线图"),
    OPEREPORTFORMSENTITY("OpeReportFormsEntity","OPE明细记录"),
    OPEREPORTFORMSALLENTITY("OpeReportFormsAllEntity","OPE合计"),
    OPEREPORTFORMSBYRATEENTITY("OpeReportFormsByRateEntity","OPE折线图"),
    PRODUCEPROGRESSREPORTEXPORTENTITY("ProduceProgressReportExportEntity","工单生产进度"),
    PRODUCEPROGRESSSCHEDULEEXPORTENTITY("ProduceProgressScheduleExportEntity","排程生产进度"),
    PRODUCEPROGRESSSUMMARYEXPORTENTITY("ProduceProgressSummaryExportEntity","生产进度汇总"),
    OEEANDOPEREPORTFORMSENTITY("OeeAndOpeReportFormsEntity","明细记录"),
    OEEANDOPEREPORTFORMSALLENTITY("OeeAndOpeReportFormsAllEntity","合计"),
    OEEANDOPEREPORTFORMSBYRATEENTITY("OeeAndOpeReportFormsByRateEntity","折线图"),
    STANDARDWORKINGHOURSEXPORT("StandardWorkingHoursExport", "标准工时主表"),
    BASISWORKINGHOURSEXPORT("BasisWorkingHoursExport", "定基工时主表"),
    ALLSTANDARDWORKINGHOURSDATAEXPORT("AllStandardWorkingHoursDataExport", "工时汇总表"),
    STATIONTRACINGCONSUMEITEMEXPORT("StationTracingConsumeItemExport","追溯物料申请明细"),
    STATIONNOTRACINGCONSUMEITEMEXPORT("StationNoTracingConsumeItemExport","非追溯物料申请明细")
    ;
    /**
     * 数据状态类型
     */
    private String className;
    /**
     * 数据状态描述信息
     */
    private String sheetName;

    /**
     * 根据key值获取value
     * @param key
     * @return
     */
    public static String getValue(String key) {
        ExportClassEnum[] exportClassEnums = values();
//        String[] keyArray = StringUtils.isEmpty(key) ? new String[]{"sheet"} :key.split("\\.");
//        key = keyArray[keyArray.length - 1];
        key = key.substring(key.lastIndexOf('.')+1);
        for (ExportClassEnum exportClassEnum : exportClassEnums) {
            if (key.equals(exportClassEnum.getClassName())) {
                return exportClassEnum.getSheetName();
            }
        }
        return "sheet";
    }
}

package org.lyflexi.customframeworkspring.commonApi.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lyflexi.customframeworkspring.commonApi.aware.IExcelRowInfoAware;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 17:02
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestExcelEntity implements IExcelRowInfoAware {
    @JsonIgnore
    public static final String SPLITTER =  ",";

    @NotBlank(message = "产品料号必选")
    @ExcelProperty(index = 0, value = "*产品料号")
    private String productMaterialCode;
    @NotBlank(message = "产品名称必选")
    @ExcelProperty(index = 1, value = "*产品名称")
    private String productMaterialName;
    @NotBlank(message = "产品版本必选")
    @ExcelProperty(index = 2, value = "*产品版本")
    private String productMaterialVersion;

    /*平铺开的字段*/
    @NotBlank(message = "lineCode不能为空")
    @ExcelProperty(index = 3, value = "*产品编码")
    private String lineCode ;
    @NotBlank(message = "lineName不能为空")
    @ExcelProperty(index = 4, value = "*产线名称")
    private String lineName ;
    @NotBlank(message = "工艺BOM编码不能为空")
    @ExcelProperty(index = 5, value = "*工艺BOM编码")
    private String operationBomCode ;
    @NotBlank(message = "operationBomName不能为空")
    @ExcelProperty(index = 6, value = "*工艺BOM名称")
    private String operationBomName ;
    @NotBlank(message = "operationBomVersion不能为空")
    @ExcelProperty(index = 7, value = "*工艺BOM版本")
    private String operationBomVersion ;
    @NotBlank(message = "routeCode不能为空")
    @ExcelProperty(index = 8, value = "*工艺路线编码")
    private String routeCode ;
    @NotBlank(message = "routeName不能为空")
    @ExcelProperty(index = 9, value = "*工艺路线名称")
    private String routeName ;
    @NotBlank(message = "routeVersion不能为空")
    @ExcelProperty(index = 10, value = "*工艺路线版本")
    private String routeVersion ;
    @NotNull(message = "是否使用SAPBOM版本不能为空")
    @ExcelProperty(index = 11, value = "*是否使用SAPBOM版本，1为是，0为否")
    private Integer enableSapBomVersion ;
    @ExcelProperty(index = 12, value = "节拍，单位：S，产出一个产品所需的时间")
    private BigDecimal lineBeat;


    /*启用字段*/
    @ExcelProperty(index = 13, value = "非必选：启用状态（-1:新建；0:启用；1-禁用）")
    private Integer enableStatus;

    @Override
    @JsonIgnore
    public String getRecordName() {
        StringBuilder sb = new StringBuilder();
        sb.append("productMaterialCode:");
        sb.append(productMaterialCode == null ? "NULL" : productMaterialCode);
        sb.append(SPLITTER+"productMaterialVersion:");
        sb.append(productMaterialVersion == null ? "NULL" : productMaterialVersion);
        return sb.toString();
    }

    @Override
    @JsonIgnore
    public String getUniqueKey() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getRecordName());
        sb.append(SPLITTER+"lineCode:");
        sb.append(lineCode == null ? "NULL" : lineCode);
        sb.append(SPLITTER+"operationBomCode:");
        sb.append(operationBomCode == null ? "NULL" : operationBomCode);
        sb.append(SPLITTER+"operationBomVersion:");
        sb.append(operationBomVersion == null ? "NULL" : operationBomVersion);
        sb.append(SPLITTER+"routeCode:");
        sb.append(routeCode == null ? "NULL" : routeCode);
        sb.append(SPLITTER+"routeVersion:");
        sb.append(routeVersion == null ? "NULL" : routeVersion);
        return sb.toString();
    }
}

package org.lyflexi.customeasyexcelframework.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.lyflexi.customeasyexcelframework.aware.IExcelRowInfoAware;
import org.lyflexi.customeasyexcelframework.exception.LyflexiErrorType;
import org.lyflexi.customeasyexcelframework.result.Result;
import org.lyflexi.customeasyexcelframework.utils.ExcelExportUtil;
import org.lyflexi.customeasyexcelframework.utils.ExcelImportUtil;
import org.lyflexi.customeasyexcelframework.vo.ExcelErrorResultVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 17:23
 */
@Slf4j
public abstract class AbstractExcelResolver<E extends IExcelRowInfoAware> implements ExcelResolver<E> {
    /**
     * 获取未分页的导出数据列表
     *
     * @param param
     * @return
     */
    protected abstract List<E> retriveDataForExport(Map<String, Object> param);

    /**
     * 筛选出可以正常导入的数据记录
     * @implNote 该方法需校验数据，但校验失败不会影响其他记录的导入
     *
     * @param excelList excel原始导入数据
     * @param toSaveList 待保存的数据列表，初始为空，需将excelList中校验成功的数据保存到该列表中
     * @return 数据异常明细
     */
    protected abstract List<ExcelErrorResultVo> convertDataForImport(List<E> excelList, List<E> toSaveList);

    /**
     * 保存导入数据
     * @param toSaveList
     */
    protected abstract void saveAllDataForImport(List<E> toSaveList);

    protected ExcelImportUtil<E, E> excelImportUtil;

    protected ExcelExportUtil<E, E> excelExportUtil;

    @Override
    public Result<String> exportData(HttpServletResponse response, HashMap<String, Object> param, Class<E> clazz,
                                     ExcelExportUtil<E, E> excelExportUtil) {
        this.excelExportUtil = excelExportUtil;
        String fileName = "PullReplenishConfigExportFile";
        List<E> list = retriveDataForExport(param);
        Map<Class, List<? extends Object>> dataMap = new HashMap<>();
        dataMap.put(clazz, list);
        try {
            excelExportUtil.exportMultipleSheetData(response, fileName, dataMap, clazz);
        } catch (Exception ex) {
            log.error("处理excel文件失败");
        }
        return Result.fail("处理excel文件失败");
    }

    @Override
    public Result<?> importData(HttpServletRequest request, HttpServletResponse response, MultipartFile file,
                                Class<E> clazz, ExcelImportUtil<E, E> excelImportUtil) {
        this.excelImportUtil = excelImportUtil;
        List<E> excelList;
        try {
            excelList = excelImportUtil.importData(clazz, file);
        } catch (Exception e) {
            log.error("import data error", e);
            return Result.fail(LyflexiErrorType.IMPORT_TEMPLATE_ERROR.getMesg());
        }
        // 导入数据合法性校验
        List<ExcelErrorResultVo> errorResultVoList = checkDuplicate(excelList);
        if (CollectionUtils.isNotEmpty(errorResultVoList)) {
            return Result.failWithData(errorResultVoList);
        }
        // 业务方向数据合法性校验
        List<E> toSaveList = new ArrayList<>(excelList.size());
        List<ExcelErrorResultVo> errorResultVoList2 = this.convertDataForImport(excelList, toSaveList);
        // 存在数据通过校验，可以导入
        if (!toSaveList.isEmpty()) {
            this.saveAllDataForImport(toSaveList);
        }
        if (CollectionUtils.isNotEmpty(errorResultVoList2)) {
            return Result.failWithData(errorResultVoList2);
        }
        return Result.success("importSuccess");
    }

    /**
     * 校验数据合法性：是否重复
     * @implNote 该方法会校验数据，校验失败会导致整个导入请求失败
     *
     * 同时校验字段非空
     * @param excelList
     * @return 数据异常明细
     */
    private List<ExcelErrorResultVo> checkDuplicate(List<E> excelList) {
        List<ExcelErrorResultVo> errorResultVoList = new ArrayList<>();
        if (excelList.isEmpty()) {
            return errorResultVoList;
        }
        ExcelErrorResultVo errorVo;

        // 导入中重复的编码
        Map<String, Long> map = excelList.stream()
                .collect(Collectors.groupingBy(o -> o.getUniqueKey(), Collectors.counting()));
        List<String> repeatCode = map.entrySet().stream()
                .filter(entry -> entry.getValue() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
        int index = 1;
        for (E entity : excelList) {
            index++;
            String errorInfo = excelImportUtil.validateExcel(entity);
            if (StringUtils.isNotBlank(errorInfo)) {
                errorVo = new ExcelErrorResultVo(index, entity.getUniqueKey(), entity.getRecordName(),
                        "新建", errorInfo);
                errorResultVoList.add(errorVo);
            } else {
                boolean isRepeat = CollectionUtils.isNotEmpty(repeatCode) && repeatCode.contains(entity.getUniqueKey());
                if (isRepeat) {
                    errorVo = new ExcelErrorResultVo(index, entity.getUniqueKey(), entity.getRecordName(),
                            "新建", "重复的导入数据");
                    errorResultVoList.add(errorVo);
                }
            }
        }
        return errorResultVoList;
    }
}

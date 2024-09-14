package org.lyflexi.customeasyexcelframework.commonApi.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 17:16
 */
@Slf4j
public class ExcelImportListener<T> extends AnalysisEventListener {

    private List<Object> list;

    public ExcelImportListener(List<T> list) {
        this.list = (List<Object>) list;
    }


    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.get(o) != null && field.get(o) != "") {
                    list.add(o);
                    return;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (this.list.size() == 0) {
            log.info("import data is empty");
        }
    }
}

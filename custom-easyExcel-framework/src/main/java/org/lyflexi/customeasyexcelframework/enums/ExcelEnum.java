package org.lyflexi.customeasyexcelframework.enums;

import org.lyflexi.customeasyexcelframework.entity.TestExcelEntity;
import org.lyflexi.customeasyexcelframework.resolver.ExcelResolver;
import org.lyflexi.customeasyexcelframework.resolver.TestExcelResolver;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 16:59
 */

public enum ExcelEnum {
    TEST_EXCEL("testExcel", TestExcelEntity.class, TestExcelResolver.class, "custom-easyExcel-framework\\src\\main\\resources\\template\\testExcel.xlsx"),
    NONE("not_exist_type", null, null, null);

    private ExcelEnum(String type, Class<?> entityClazz, Class<? extends ExcelResolver<?>> clazz,
                      String templateLocation) {
        this.type = type;
        this.entityClazz = entityClazz;
        this.resolverClazz = clazz;
        this.templateLocation = templateLocation;
    }

    /** 配置类型 */
    private String type;
    /** Excel行记录对应的Entity类 */
    private Class<?> entityClazz;
    /** Excel行记录的解析实现类 */
    private Class<? extends ExcelResolver<?>> resolverClazz;
    /** 模板文件的物理路径 */
    private String templateLocation;

    public String getType() {
        return type;
    }

    public Class<?> getEntityClazz() {
        return entityClazz;
    }

    public Class<? extends ExcelResolver<?>> getResolverClazz() {
        return resolverClazz;
    }

    public String getTemplateLocation() {
        return templateLocation;
    }

    public static ExcelEnum match(String type) {
        for (ExcelEnum excelEnum : values()) {
            if (excelEnum.type.equals(type)) {
                return excelEnum;
            }
        }
        return NONE;
    }
}

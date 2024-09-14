package org.lyflexi.customframeworkspring.commonApi.enums;


import org.lyflexi.customframeworkspring.commonApi.entity.TestExcelEntity;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 16:59
 */

public enum ExcelEnum {
    TEST_EXCEL("testExcel", TestExcelEntity.class, "testExcelResolver", "template\\testExcel.xlsx"),
    NONE("not_exist_type", null, null, null);

    private ExcelEnum(String type, Class<?> entityClazz, String resolverName,
                      String templateLocation) {
        this.type = type;
        this.entityClazz = entityClazz;
        this.resolverName = resolverName;
        this.templateLocation = templateLocation;
    }

    /** 配置类型 */
    private String type;
    /** Excel行记录对应的Entity类 */
    private Class<?> entityClazz;
    /** Excel行记录的解析实现类名 */
    private String resolverName;
    /** 模板文件的物理路径 */
    private String templateLocation;

    public String getType() {
        return type;
    }

    public Class<?> getEntityClazz() {
        return entityClazz;
    }

    public String GetResolverName() {
        return resolverName;
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

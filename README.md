# easyExcel-practice

# 可配置化
需求：我们希望在Controller层只做1组导入导出接口，适配无数的Web页面，这样的好处是避免Controller层的导入导出接口泛滥

思路：对于每个Web页面的导入导出逻辑，根据请求传参type的不同，后台都有相应的解析器Resolver来处理

接下来有两种设计思路：
## 设计方案1：
实现出所有页面导入导出对应的Resolver，并打上@Component注解，初始化项目阶段装载所有的的Resolver实例至IOC容器

为了做到配置化，我们需要提前准备好所有的[请求参数type，对应Resolver实例名称]的枚举信息Enum

在导入导出的逻辑中，根据请求参数type，通过Spring-API来获取对应BeanName的Resolver来处理任务

这种方式的优点是任务执行之前Resolver就已经实例化好了，缺点是要在项目启动阶段就就创建全部的Resolver实例会耗更多的内存
## 设计方案2：
依然是为了做到配置化，我们需要提前准备好所有的[type，对应解析器Resolver.class]的枚举信息，
```java
TEST_EXCEL("testExcel", TestExcelEntity.class, TestExcelResolver.class, "template\\testExcel.xlsx"),
NONE("not_exist_type", null, null, null);
```

与方案1不同的地方是，只编写好页面导入导出对应的解析器Resolver，但不是将所有的Resolver一股脑全实例化到Spring容器中，而是在请求到来之后根据请求参数type，获取指定的Resolver.class对象，然后和SpringIOC一样的实现原理，手动通过反射机制创建出该Resolver的实例对象，来处理具体的导入导出任务

这种方式的优点是每次按需创建Resolver实例比较节省内存，缺点是在请求到来之后再通过反射实例化Resolver效率会比较低

总的来说，任何设计方案乃至架构，到最后都是性能和内存之间的一种取舍（升华了，哈哈哈）

对于本项目来说，只是为了让大家学习设计思想，因此用哪种方案都可以.

最终本项目仅以方案2为例来做示范，感兴趣的朋友也可以借鉴本项目的代码与工程结构，对方案1做出实现

在吃透了任意一种方案之后，另一种思路实现起来应该就不难了


# 设计模式分析

抽象类常用于骨架设计，提供一组通用的公共方法。

但是抽象类的定义中是存在，未被实现的abstract方法待子类去实现，那么这些abstract方法和Interface中的接口方法在设计上有什么区别呢？

这是接口和抽象类的区别，应该没有异议：

> 接口：更倾向于表示能力或行为的集合，强调的是“能做什么”。
> 
> 抽象类：更倾向于表示一个具体的概念，给出初版的接口实现，强调的是“是什么”

但是，依旧没有解释Abstract类中的abstract方法和Interface中的接口方法在设计上有什么区别呢？下面我个人一言以蔽之：
- Interface中的接口方法用于对外部提供访问的途径
- Abstract类中的abstract方法用于对内提供实现的途径

具体而言，看如下的设计思想：

对外提供ExcelResolver接口：[ExcelResolver.java](custom-easyExcel-framework%2Fsrc%2Fmain%2Fjava%2Forg%2Flyflexi%2Fcustomeasyexcelframework%2FcommonApi%2Fresolver%2FExcelResolver.java)ExcelResolver

```java
public interface ExcelResolver<E> {
    /**
     * 导入数据
     * @param request
     * @param response
     * @param file
     * @param clazz
     * @param excelImportUtil
     * @return
     */
    Result<?> importData(HttpServletRequest request, HttpServletResponse response, MultipartFile file,
                         Class<E> clazz, ExcelImportUtil<E, E> excelImportUtil);
    /**
     * excel导出数据
     * @param param
     * @param clazz
     * @param response
     * @param excelImportUtil
     */
    Result<String> exportData(HttpServletResponse response, HashMap<String, Object> param, Class<E> clazz,
                              ExcelExportUtil<E, E> excelImportUtil);
}

```

抽象类AbstractExcelResolver实现上述接口：[AbstractExcelResolver.java](custom-easyExcel-framework%2Fsrc%2Fmain%2Fjava%2Forg%2Flyflexi%2Fcustomeasyexcelframework%2FcommonApi%2Fresolver%2FAbstractExcelResolver.java)
```java
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

```

在抽象类AbstractExcelResolver实现ExcelResolver接口的时候，抽取了三个方法：
- abstract retriveDataForExport
- abstract convertDataForImport
- abstract saveAllDataForImport

因此这三个方法就是对内的抽象方法，这就是抽象方法与ExcelResolver接口方法的设计区别

package org.lyflexi.customeasyexcelframework.commonApi.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.lyflexi.customeasyexcelframework.commonApi.listener.ExcelImportListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/4 17:11
 */
@Slf4j
@Component
public class ExcelImportUtil<T, E> {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static final String SEPARATOR = System.getProperty("file.separator");

    public List<T> importData(Class<E> clazz, MultipartFile file) throws Exception {
        return importData((T) clazz.newInstance(), clazz, file);
    }

    /**
     * @param t
     * @param clazz
     * @param file
     * @return
     */
    public List<T> importData(T t, Class<E> clazz, MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = null;
        if (StrUtil.isNotBlank(fileName)) {
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }
        log.info("文件的后缀名为：" + suffixName);
        //创建一个临时文件，用于暂时存放
        File tmpFile = null;
        FileInputStream inputStream = null;
        List<T> list = new ArrayList<>();
        File directory = new File(".");
        String path = directory.getCanonicalPath();// 获取当前路径
        path = path + SEPARATOR + "tmp";
        try {
            tmpFile = File.createTempFile(path, null);
            file.transferTo(tmpFile);
            inputStream = new FileInputStream(tmpFile);
            EasyExcel.read(tmpFile,new ExcelImportListener(list)).head(clazz).sheet().doReadSync();
            if (CollectionUtils.isNotEmpty(list)) {
                list.removeIf(ObjectUtil::isAllEmpty);
            }
        } catch (IOException e) {
            log.error("导入报错");
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
            if (null != tmpFile && tmpFile.exists()) {
                boolean delete = tmpFile.delete();
                log.info("删除文件结果={}", delete);
            }
        }
        return list;
    }

    /**
     * 校验Excel数据实体对象注解非空以及有效性
     *
     * @return
     */
    public String validateExcel(T t) {
        StringBuilder sb = new StringBuilder();
        String separator = ";";
        try {
            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(t);
            for (ConstraintViolation<Object> c : constraintViolations) {
                String message = c.getMessage();
                if(!StringUtils.isEmpty(message) && message.matches("^MV.*\\d{5,15}$")){
                    try {
//                        String internationMessage = ResourceInternationalHelper.getByKey(message);
//                        if (!StringUtils.isEmpty(internationMessage)) {
//                            message = internationMessage;
//                        }
                    } catch (Exception e) {
                    }
                }
                sb.append(message);
                sb.append(separator);
            }
        } catch (IllegalArgumentException e) {
            log.error("excel 校验数据错误，错误信息={}", e.getMessage());
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    /**
     * @param file
     * @return
     */
    public Map<String, Object> importThreeSheetData(Class<E> bomClazz, Class<E> materialClazz, Class<E> replaceClazz, MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = null;
        if (StrUtil.isNotBlank(fileName)) {
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }
        log.info("文件的后缀名为：" + suffixName);
        //创建一个临时文件，用于暂时存放
        File directory = new File(".");
        String path = directory.getCanonicalPath();// 获取当前路径
        path = path + SEPARATOR + "tmp";
        File tmpFile = null;
        FileInputStream inputStream = null;
        List<List<T>> list = null;
        List<Object> mainExcelList = new ArrayList<>();
        List<Object> materialExcelList = new ArrayList<>();
        List<Object> replaceMaterialExcelList = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        try {
            tmpFile = File.createTempFile(path, null);
            file.transferTo(tmpFile);
            inputStream = new FileInputStream(tmpFile);

            EasyExcel.read(tmpFile, new ExcelImportListener(mainExcelList)).head(bomClazz).sheet(0).doReadSync();
            EasyExcel.read(tmpFile, new ExcelImportListener(materialExcelList)).head(materialClazz).sheet(1).doReadSync();
            EasyExcel.read(tmpFile, new ExcelImportListener(replaceMaterialExcelList)).head(replaceClazz).sheet(2).doReadSync();

            if (CollectionUtils.isNotEmpty(mainExcelList)) {
                mainExcelList.removeIf(ObjectUtil::isAllEmpty);
            }
            if (CollectionUtils.isNotEmpty(materialExcelList)) {
                materialExcelList.removeIf(ObjectUtil::isAllEmpty);
            }
            if (CollectionUtils.isNotEmpty(replaceMaterialExcelList)) {
                replaceMaterialExcelList.removeIf(ObjectUtil::isAllEmpty);
            }
            map.put("0", mainExcelList);
            map.put("1", materialExcelList);
            map.put("2", replaceMaterialExcelList);
        } catch (IOException e) {
            log.error("导入报错");
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
            if (null != tmpFile && tmpFile.exists()) {
                boolean delete = tmpFile.delete();
                log.info("删除文件结果={}", delete);
            }
        }
        return map;
    }

    /**
     * 多个子页签上传功能
     *
     * @param file
     * @param classArgs
     * @return
     * @throws Exception
     */
    public Map<String, Object> importMultipleSheetData(MultipartFile file, Class<E>... classArgs) throws Exception {
        String fileName = file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = null;
        if (StrUtil.isNotBlank(fileName)) {
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }
        log.info("文件的后缀名为：" + suffixName);
        //创建一个临时文件，用于暂时存放
        File directory = new File(".");
        String path = directory.getCanonicalPath();// 获取当前路径
        path = path + SEPARATOR + "tmp";
        File tmpFile = null;
        FileInputStream inputStream = null;
        List<List<T>> list = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        try {
            tmpFile = File.createTempFile(path, null);
            file.transferTo(tmpFile);
            inputStream = new FileInputStream(tmpFile);

            for (int i = 0; i< classArgs.length; i++) {
                list = EasyExcel.read(tmpFile).head(classArgs[i]).sheet(i).doReadSync();
                map.put(String.valueOf(i), list);
            }
        } catch (IOException e) {
            log.error("导入报错");
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
            if (null != tmpFile && tmpFile.exists()) {
                boolean delete = tmpFile.delete();
                log.info("删除文件结果={}", delete);
            }
        }
        return map;
    }
}

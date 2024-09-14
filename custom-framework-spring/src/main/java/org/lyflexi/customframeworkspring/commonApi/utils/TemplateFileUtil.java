package org.lyflexi.customframeworkspring.commonApi.utils;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.lyflexi.customframeworkspring.commonApi.entity.TemplateDownloadParam;
import org.lyflexi.customframeworkspring.commonApi.enums.ExcelEnum;
import org.lyflexi.customframeworkspring.commonApi.exception.LyflexiErrorType;
import org.lyflexi.customframeworkspring.commonApi.result.Result;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @Description:
 * @Author: lyflexi
 * @project: easyExcel-practice
 * @Date: 2024/9/5 9:50
 */
@Slf4j
@Component
public class TemplateFileUtil {
    public Result download(TemplateDownloadParam templateDownloadParam, HttpServletResponse response) {

        if( templateDownloadParam == null || StrUtil.isBlank(templateDownloadParam.getTemplateName()) ){
            return Result.fail(LyflexiErrorType.INCOMPLETE_PARAM);
        }
        //获取要下载的模板名称
        String fileName = templateDownloadParam.getTemplateName();
        //使用类加载器来获取资源路径会更加可靠，这样可以确保即使在不同的部署环境下，资源文件也能被正确加载。
        ClassLoader classLoader = getClass().getClassLoader();
//        URL resource = classLoader.getResource("template/" + fileName+".xlsx");
        URL resource = classLoader.getResource(ExcelEnum.match(fileName).getTemplateLocation());
        if (resource==null){
            return Result.fail(LyflexiErrorType.SERVICE_RESOURCE_NOT_EXIST);
        }
        log.info("just for test step 1:"+ resource.getPath());

        try {
            //设置头文件
            response.setHeader("Content-disposition", "attachment;fileName=" + fileName);
            //设置文件传输类型
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //IO流处理模板
            log.info("just for test step2:"+ resource.getPath());
            InputStream input = resource.openStream();
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[2048];
            int len;
            while ((len = input.read(b)) != -1) {
                out.write(b,0,len);
            }
            //返回请求访问的结果
            response.setHeader("Content-Length", String.valueOf(input.available()));
            input.close();
        } catch (Exception e) {
            log.error("getApplicationTemplate :", e);
            return Result.fail(e.getMessage());
        }

        return Result.success();
    }
}

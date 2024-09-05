package org.lyflexi.customeasyexcelframework.utils;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @Description:
 * @Author: lyflexi
 * @project: debuginfo_jdkToFramework
 * @Date: 2024/8/15 13:32
 */
@Component
public class SpringUtils implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(SpringUtils.class);
    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext (@Autowired ApplicationContext applicationContext) {
        SpringUtils.applicationContext = applicationContext;
    }

    public SpringUtils() {
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBeanElseNull(Class<T> clazz) {
        try {
            return getApplicationContext().getBean(clazz);
        } catch (Exception e) {
            log.info("bean {} 不存在！", clazz);
            return null;
        }
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    public static <T> T getBeanIfAbsent(Class<T> clazz) {
        T bean = null;
        Map<String, T> beanMap = getApplicationContext().getBeansOfType(clazz);
        if (!CollectionUtils.isEmpty(beanMap)) {
            bean = getApplicationContext().getBean(clazz);
        }

        return bean;
    }
}
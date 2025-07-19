package com.bookstore.demo.utils;

import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

public class ReflectiveUtils {

    @SneakyThrows
    public static void setFieldForObject(Object object, String fieldName, Object fieldValue) {
        BeanUtils.setProperty(object, fieldName, fieldValue);
    }

    @SneakyThrows
    public static void setFieldsForObject(Object object, Map<String, Object> fieldValueMap) {
        fieldValueMap.forEach((field, value) -> setFieldForObject(object, field, value));
    }
}

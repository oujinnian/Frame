package com.jno.cloud.framework.util.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 简单封装属性拷贝
 * 对象字段操作
 */
@Slf4j
@UtilityClass
public class PropertyCopyUtils {

    /**
     * 获得值为null或类型为String时为blank的属性名称
     *
     * @param o
     * @return
     */
    public String[] getNullProperties(Object o) {
        if (o != null) {
            Class clazz = o.getClass();
            Field[] fields = FieldUtils.getAllFields(clazz);
            return Arrays.stream(fields).filter(e -> {
                String type = e.getGenericType().toString();
                e.setAccessible(true);
                try {
                    Object v = e.get(o);
                    return v == null || (type.equals("class java.lang.String") && isBlank((String) v));
                } catch (IllegalAccessException ex) {
                    log.error("属性获取出错{}->" + ex.getMessage());
                    ex.printStackTrace();
                }
                return false;
            }).map(e -> e.getName()).toArray(String[]::new);
        }
        return null;
    }

    /**
     * 两个对象拷贝时忽略null和字符串为空字符的字段
     *
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public <T> T copyNullProperties(Object source, T target) {
        BeanUtils.copyProperties(source, target, getNullProperties(source));
        return target;
    }

    /**
     * 将未知对象转为json对象
     *
     * @param o
     * @return
     */
    public JSONObject objectToJson(Object o) {
        return o == null ? new JSONObject() : JSONObject.parseObject(JSON.toJSONString(o));
    }

    /**
     * 将某些字段置为null
     *
     * @param obj
     * @param fields
     * @return
     */
    public Object resetFiled(Object obj, String... fields) {
        if (fields.length > 0) {
            JSONObject json = objectToJson(obj);
            Arrays.stream(fields).forEach(e -> json.put(e, null));
            return json;
        }
        return obj;
    }
}

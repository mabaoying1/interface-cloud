package com.bsoft.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：hxy
 * @date ：Created in 2019/09/27 10:13
 * @description：实体类转换Map Map转换实体类
 * @modified By：
 * @version: $
 */

public class JavaBeanUtil {
    private static Logger logger = LoggerFactory.getLogger(JavaBeanUtil.class);


    /**
      * 实体类转map
      * @param obj
      * @return
      */
            public static Map<String, Object> convertBeanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if(null==value){
                        map.put(key,"");
                    }else{
                        map.put(key,value);
                    }
                }


            }
        } catch (Exception e) {
            logger.error("convertBean2Map Error {}" ,e);
        }
        return map;
    }


    /**
      * map 转实体类
      * @param clazz
      * @param map
      * @param <T>
      * @return
      */
            public static <T> T convertMapToBean(Class<T> clazz, Map<String,Object> map) {
        T obj = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            obj = clazz.newInstance(); // 创建 JavaBean 对象


            // 给 JavaBean 对象的属性赋值
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                    Object value = map.get(propertyName);
                    if ("".equals(value)) {
                        value = null;
                    }
                    Object[] args = new Object[1];
                    args[0] = value;
                    descriptor.getWriteMethod().invoke(obj, args);


                }
            }
        } catch (IllegalAccessException e) {
            logger.error("convertMapToBean 实例化JavaBean失败 Error{}" ,e);
        } catch (IntrospectionException e) {
            logger.error("convertMapToBean 分析类属性失败 Error{}" ,e);
        } catch (IllegalArgumentException e) {
            logger.error("convertMapToBean 映射错误 Error{}" ,e);
        } catch (InstantiationException e) {
            logger.error("convertMapToBean 实例化 JavaBean 失败 Error{}" ,e);
        }catch (InvocationTargetException e){
            logger.error("convertMapToBean字段映射失败 Error{}" ,e);
        }catch (Exception e){
            logger.error("convertMapToBean Error{}" ,e);
        }
        return (T) obj;
    }


}

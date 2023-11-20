package com.tohant.om2d.di;

import com.tohant.om2d.di.annotation.BeanType;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.di.annotation.Configuration;
import com.tohant.om2d.di.annotation.Dao;

public class DIUtil {

    public static boolean isSingleton(Class<?> implementation) {
        return (implementation.isAnnotationPresent(Component.class) &&
                implementation.getAnnotation(Component.class).value() == BeanType.SINGLETON)
                || (implementation.isAnnotationPresent(Configuration.class) &&
                implementation.getAnnotation(Configuration.class).value() == BeanType.SINGLETON)
                || (implementation.isAnnotationPresent(Dao.class) &&
                implementation.getAnnotation(Dao.class).value() == BeanType.SINGLETON);
    }

    public static boolean hasAnyBeanAnnotation(Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Configuration.class)
                || clazz.isAnnotationPresent(Dao.class);
    }

}

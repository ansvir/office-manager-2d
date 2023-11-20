package com.tohant.om2d.di;

import com.tohant.om2d.di.annotation.Value;

import java.lang.reflect.Field;

public class PropertyInjectorUtil {

    public static void injectProperties(Object target, ConfigSource configSource) throws IllegalAccessException {
        Class<?> clazz = target.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Value.class)) {
                Value value = field.getAnnotation(Value.class);
                String propertyName = value.value();
                String propertyValue = configSource.getProperty(propertyName);

                if (propertyValue != null) {
                    field.setAccessible(true);
                    field.set(target, propertyValue);
                }
            }
        }
    }
}
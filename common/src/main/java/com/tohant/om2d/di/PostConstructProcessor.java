package com.tohant.om2d.di;

import com.tohant.om2d.di.annotation.PostConstruct;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostConstructProcessor {

    private final List<Object> beans;

    public PostConstructProcessor() {
        beans = new ArrayList<>();
    }

    public void addBean(Object bean) {
        beans.add(bean);
    }

    public void processPostConstructMethods() throws InvocationTargetException, IllegalAccessException {
        for (Object bean : beans) {
            Class<?> beanClass = bean.getClass();
            for (Method method : beanClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(PostConstruct.class)) {
                    method.setAccessible(true);
                    method.invoke(bean);
                }
            }
        }
    }

}
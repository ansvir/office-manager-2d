package com.tohant.om2d.di.util;

import lombok.Getter;

@Getter
public abstract class BaseDaoImpl {

    private final Class<?> clazz;

    public BaseDaoImpl(Class<?> clazz) {
        this.clazz = clazz;
    }

}

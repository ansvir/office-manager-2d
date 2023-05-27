package com.tohant.om2d.common.storage;

public class Field {

    private final String name;
    private final Class<?> valueType;

    public Field(String name, Class<?> valueType) {
        this.name = name;
        this.valueType = valueType;
    }

    public String getName() {
        return name;
    }

    public Class<?> getValueType() {
        return valueType;
    }

}

package com.tohant.om2d.storage.database.sqlite;

import com.tohant.om2d.common.storage.Field;

public class SQLiteField extends Field {

    private final Type type;
    private final Object value;

    public SQLiteField(String name, Type type, Class<?> valueType, Object value) {
        super(name, valueType);
        this.type = type;
        this.value = value;
    }

    public enum Type {
        TEXT, INTEGER, REAL, NULL, BLOB
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

}

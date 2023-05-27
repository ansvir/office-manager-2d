package com.tohant.om2d.common.storage;

import com.tohant.om2d.common.Identifiable;

import java.util.Collection;
import java.util.UUID;

public abstract class Entity extends Identifiable {

    private final String TABLE_NAME;
    private final Collection<Field> FIELDS;

    public Entity(UUID id, String tableName, Collection<Field> fields) {
        super(id);
        this.TABLE_NAME = tableName;
        this.FIELDS = fields;
    }

    public Entity(String tableName, Collection<Field> fields) {
        this.TABLE_NAME = tableName;
        this.FIELDS = fields;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public Collection<Field> getFields() {
        return FIELDS;
    }

}

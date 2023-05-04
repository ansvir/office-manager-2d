package com.tohant.om2d.storage;

public interface Cache {

    void setValue(String key, Object value);

    Object getValue(String key);

}

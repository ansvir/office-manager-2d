package com.tohant.om2d.storage;

import com.badlogic.gdx.utils.Array;

import java.util.Optional;

public interface Database<T> {

    Optional<T> getById(String id);
    Array<T> getAll();
    void save(T t);
    void saveAll(Array<T> t);
    void deleteById(String id);
    void deleteAll();

}

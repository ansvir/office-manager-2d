package com.tohant.om2d.storage.repository;

import com.tohant.om2d.common.storage.Entity;
import com.tohant.om2d.common.storage.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class JsonFileRepository implements Repository {

    @Override
    public void saveAll(Collection<Entity> e) {

    }

    @Override
    public void save(Entity e) {

    }

    @Override
    public void update(Entity e) {

    }

    @Override
    public Collection<Entity> findAll(String tableName) {
        return null;
    }

    @Override
    public Optional<Entity> findById(String tableName, UUID id) {
        return Optional.empty();
    }

}

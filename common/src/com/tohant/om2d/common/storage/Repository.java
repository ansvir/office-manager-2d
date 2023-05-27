package com.tohant.om2d.common.storage;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface Repository {

    void saveAll(Collection<Entity> e);

    void save(Entity e);

    void update(Entity e);

    Collection<Entity> findAll(String tableName);

    Optional<Entity> findById(String tableName, UUID id);

}

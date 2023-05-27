package com.tohant.om2d.storage.repository;

import com.tohant.om2d.common.storage.Entity;
import com.tohant.om2d.common.storage.Field;
import com.tohant.om2d.common.storage.Repository;
import com.tohant.om2d.storage.database.sqlite.SQLiteConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

public class SQLiteRepository implements Repository {

    private static final String INSERT_SQL = "INSERT INTO %s (%s) VALUES (%s);";
    private static final String UPDATE_SQL = "UPDATE ";
    private static final String SELECT_SQL = "SELECT * FROM %s";

    private final SQLiteConnector connector;

    public SQLiteRepository() {
        this.connector = SQLiteConnector.getInstance();
    }

    @Override
    public void saveAll(Collection<Entity> e) {
        if (!e.isEmpty()) {
            Statement statement = this.connector.getNewStatement();
            try {
                for (Entity ee : e) {
                    statement.addBatch(String.format(INSERT_SQL, ee.getTableName(),
                            buildNames(ee.getFields()), buildValues(ee.getFields())));
                }
                statement.executeBatch();
            } catch (SQLException ex) {
                System.err.println("CANNOT PERFORM INSERT OPERATION. CAUSE: " + ex.getMessage());
            }
        }
    }

    @Override
    public void save(Entity e) {
        Statement statement = this.connector.getNewStatement();
        try {
                statement.execute(String.format(INSERT_SQL, e.getTableName(),
                        buildNames(e.getFields()), buildValues(e.getFields())));
        } catch (SQLException ex) {
            System.err.println("CANNOT PERFORM INSERT OPERATION. CAUSE: " + ex.getMessage());
        }
    }

    @Override
    public void update(Entity e) {
        Statement statement = this.connector.getNewStatement();
        try {
            statement.execute(String.format(INSERT_SQL, e.getTableName(),
                    buildNames(e.getFields()), buildValues(e.getFields())));
        } catch (SQLException ex) {
            System.err.println("CANNOT PERFORM UPDATE OPERATION. CAUSE: " + ex.getMessage());
        }
    }

    @Override
    public Collection<Entity> findAll(String tableName) {
        Statement statement = this.connector.getNewStatement();
        try {
            ResultSet result = statement.executeQuery(String.format(SELECT_SQL, tableName));
//            Entity entity = getEntityByTableName(tableName);
        } catch (SQLException ex) {
            System.err.println("CANNOT PERFORM INSERT OPERATION. CAUSE: " + ex.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Entity> findById(String tableName, UUID id) {
        return Optional.empty();
    }

    private String buildNames(Collection<Field> fields) {
        StringBuilder result = new StringBuilder();
        for (Field f : fields) {
            result.append(f.getName());
            result.append(", ");
        }
        return result.substring(0, result.length() - 3);
    }

    private String buildValues(Collection<Field> fields) {
        StringBuilder result = new StringBuilder();
        for (Field f : fields) {
            result.append(f.getName());
            result.append(" ");
            result.append(", ");
        }
        return result.substring(0, result.length() - 3);
    }

}

package com.tohant.om2d.storage.database;

import com.badlogic.gdx.Gdx;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tohant.om2d.model.entity.*;

import java.sql.SQLException;

public class SQLiteDatabaseHelper {

    private static final String DATABASE_URL = "jdbc:sqlite:data/db/om2d.db";
    private static SQLiteDatabaseHelper instance;
    private ConnectionSource connectionSource;

    private SQLiteDatabaseHelper() {
        try {
            if (!Gdx.files.local("data/db").exists()) {
                Gdx.files.local("data/db").mkdirs();
            }
            connectionSource = new JdbcConnectionSource(DATABASE_URL);
            TableUtils.createTableIfNotExists(connectionSource, WorkerEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, RoomEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, ResidentEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, CellEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, LevelEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, OfficeEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, CompanyEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, ProgressEntity.class);
        } catch (SQLException e) {
            Gdx.app.error("DATABASE CONNECTION", "CANNOT OBTAIN DATABASE CONNECTION, CAUSE: " + e.getLocalizedMessage());
        }
    }

    public static SQLiteDatabaseHelper getInstance() {
        if (instance == null) {
            instance = new SQLiteDatabaseHelper();
        }
        return instance;
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

}

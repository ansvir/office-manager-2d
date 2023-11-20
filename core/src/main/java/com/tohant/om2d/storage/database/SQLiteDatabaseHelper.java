package com.tohant.om2d.storage.database;

import com.badlogic.gdx.Gdx;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.di.annotation.PostConstruct;
import com.tohant.om2d.model.entity.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@Getter
@Component
public class SQLiteDatabaseHelper {

    private static final String DATABASE_URL = "jdbc:sqlite:data/db/om2d.db";
    private ConnectionSource connectionSource;

    @PostConstruct
    public void init() {
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

}

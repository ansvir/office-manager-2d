package com.tohant.om2d.storage.database;

import com.badlogic.gdx.Gdx;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.tohant.om2d.di.annotation.Dao;
import com.tohant.om2d.model.entity.LevelEntity;
import com.tohant.om2d.model.entity.OfficeEntity;
import com.tohant.om2d.model.entity.ResidentEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Dao
public class ResidentDao extends BaseDaoImpl<ResidentEntity, UUID> {

    public ResidentDao(SQLiteDatabaseHelper sqLiteDatabaseHelper) throws SQLException {
        super(sqLiteDatabaseHelper.getConnectionSource(), ResidentEntity.class);
    }

    @Override
    public ResidentEntity queryForId(UUID s) {
        try {
            return super.queryForId(s);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT QUERY FOR ID", "CAUSE: " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<ResidentEntity> queryForAll() {
        try {
            return super.queryForAll();
        } catch (SQLException e) {
            Gdx.app.error("CANNOT QUERY FOR ALL", "CAUSE: " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public int create(ResidentEntity data) {
        try {
            return super.create(data);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT CREATE", "CAUSE: " + e.getLocalizedMessage());
            return 0;
        }
    }

    @Override
    public int deleteById(UUID s) {
        try {
            return super.deleteById(s);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT DELETE BY ID", "CAUSE: " + e.getLocalizedMessage());
            return 0;
        }
    }

}

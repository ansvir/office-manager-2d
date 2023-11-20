package com.tohant.om2d.storage.database;

import com.badlogic.gdx.Gdx;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.tohant.om2d.di.annotation.Dao;
import com.tohant.om2d.model.entity.CellEntity;
import com.tohant.om2d.model.entity.LevelEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Dao
public class CellDao extends BaseDaoImpl<CellEntity, UUID> {

    public CellDao(SQLiteDatabaseHelper sqLiteDatabaseHelper) throws SQLException {
        super(sqLiteDatabaseHelper.getConnectionSource(), CellEntity.class);
    }

    @Override
    public CellEntity queryForId(UUID s) {
        try {
            return super.queryForId(s);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT QUERY FOR ID", "CAUSE: " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<CellEntity> queryForAll() {
        try {
            return super.queryForAll();
        } catch (SQLException e) {
            Gdx.app.error("CANNOT QUERY FOR ALL", "CAUSE: " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public long countOf()  {
        try {
            return super.countOf();
        } catch (SQLException e) {
            Gdx.app.error("CANNOT COUNT OF", "CAUSE: " + e.getLocalizedMessage());
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

    @Override
    public int create(CellEntity data) {
        try {
            return super.create(data);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT CREATE", "CAUSE: " + e.getLocalizedMessage());
            return 0;
        }
    }

    @Override
    public int update(CellEntity data) {
        try {
            return super.update(data);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT UPDATE", "CAUSE: " + e.getLocalizedMessage());
            return 0;
        }
    }
}

package com.tohant.om2d.storage.database;

import com.badlogic.gdx.Gdx;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.tohant.om2d.di.annotation.Dao;
import com.tohant.om2d.di.annotation.PostConstruct;
import com.tohant.om2d.model.entity.CompanyEntity;
import com.tohant.om2d.model.entity.LevelEntity;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Dao
public class CompanyDao extends BaseDaoImpl<CompanyEntity, UUID> {

    public CompanyDao(SQLiteDatabaseHelper sqLiteDatabaseHelper) throws SQLException {
        super(sqLiteDatabaseHelper.getConnectionSource(), CompanyEntity.class);
    }

    @Override
    public CompanyEntity queryForId(UUID s) {
        try {
            return super.queryForId(s);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT QUERY FOR ID", "CAUSE: " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<CompanyEntity> queryForAll() {
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
    public int create(CompanyEntity data) {
        try {
            return super.create(data);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT CREATE", "CAUSE: " + e.getLocalizedMessage());
            return 0;
        }
    }

    @Override
    public int update(CompanyEntity data) {
        try {
            return super.update(data);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT UPDATE", "CAUSE: " + e.getLocalizedMessage());
            return 0;
        }
    }

}

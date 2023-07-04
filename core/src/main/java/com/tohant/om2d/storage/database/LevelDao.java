package com.tohant.om2d.storage.database;

import com.badlogic.gdx.Gdx;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.tohant.om2d.model.entity.CellEntity;
import com.tohant.om2d.model.entity.LevelEntity;
import com.tohant.om2d.model.entity.ProgressEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class LevelDao extends BaseDaoImpl<LevelEntity, UUID> {

    private static LevelDao instance;

    public LevelDao() throws SQLException {
        super(SQLiteDatabaseHelper.getInstance().getConnectionSource(), LevelEntity.class);
    }

    public static LevelDao getInstance() {
        if (instance == null) {
            try {
                instance = new LevelDao();
            } catch (SQLException e) {
                Gdx.app.error("DAO INSTANTIATION EXCEPTION", "CANNOT INSTANTIATE LEVEL DAO, CAUSE: "
                        + e.getLocalizedMessage());
            }
        }
        return instance;
    }

    @Override
    public LevelEntity queryForId(UUID s) {
        try {
            return super.queryForId(s);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT QUERY FOR ID", "CAUSE: " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<LevelEntity> queryForAll() {
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
    public int create(LevelEntity data) {
        try {
            return super.create(data);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT CREATE", "CAUSE: " + e.getLocalizedMessage());
            return 0;
        }
    }

}

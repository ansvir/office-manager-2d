package com.tohant.om2d.storage.database;

import com.badlogic.gdx.Gdx;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.tohant.om2d.model.entity.CellEntity;
import com.tohant.om2d.model.entity.CompanyEntity;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.model.entity.WorkerEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ProgressDao extends BaseDaoImpl<ProgressEntity, UUID> {

    private static ProgressDao instance;

    public ProgressDao() throws SQLException {
        super(SQLiteDatabaseHelper.getInstance().getConnectionSource(), ProgressEntity.class);
    }

    public static ProgressDao getInstance() {
        if (instance == null) {
            try {
                instance = new ProgressDao();
            } catch (SQLException e) {
                Gdx.app.error("DAO INSTANTIATION EXCEPTION", "CANNOT INSTANTIATE PROGRESS DAO, CAUSE: "
                        + e.getLocalizedMessage());
            }
        }
        return instance;
    }

    @Override
    public ProgressEntity queryForId(UUID s) {
        try {
            return super.queryForId(s);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT QUERY FOR ID", "CAUSE: " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<ProgressEntity> queryForAll() {
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
    public int create(ProgressEntity data) {
        try {
            return super.create(data);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT CREATE", "CAUSE: " + e.getLocalizedMessage());
            return 0;
        }
    }

}

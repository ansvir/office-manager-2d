package com.tohant.om2d.storage.database;

import com.badlogic.gdx.Gdx;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.tohant.om2d.model.entity.OfficeEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class OfficeDao extends BaseDaoImpl<OfficeEntity, UUID> {

    private static OfficeDao instance;

    public OfficeDao() throws SQLException {
        super(SQLiteDatabaseHelper.getInstance().getConnectionSource(), OfficeEntity.class);
    }

    public static OfficeDao getInstance() {
        if (instance == null) {
            try {
                instance = new OfficeDao();
            } catch (SQLException e) {
                Gdx.app.error("DAO INSTANTIATION EXCEPTION", "CANNOT INSTANTIATE OFFICE DAO, CAUSE: "
                        + e.getLocalizedMessage());
            }
        }
        return instance;
    }

    @Override
    public OfficeEntity queryForId(UUID s) {
        try {
            return super.queryForId(s);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT QUERY FOR ID", "CAUSE: " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<OfficeEntity> queryForAll() {
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
    public int create(OfficeEntity data) {
        try {
            return super.create(data);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT CREATE", "CAUSE: " + e.getLocalizedMessage());
            return 0;
        }
    }

}

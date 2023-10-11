package com.tohant.om2d.storage.database;

import com.badlogic.gdx.Gdx;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.tohant.om2d.model.entity.ResidentEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ResidentDao extends BaseDaoImpl<ResidentEntity, UUID> {

    private static ResidentDao instance;

    public ResidentDao() throws SQLException {
        super(SQLiteDatabaseHelper.getInstance().getConnectionSource(), ResidentEntity.class);
    }

    public static ResidentDao getInstance() {
        if (instance == null) {
            try {
                instance = new ResidentDao();
            } catch (SQLException e) {
                Gdx.app.error("DAO INSTANTIATION EXCEPTION", "CANNOT INSTANTIATE RESIDENT DAO, CAUSE: "
                        + e.getLocalizedMessage());
            }
        }
        return instance;
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

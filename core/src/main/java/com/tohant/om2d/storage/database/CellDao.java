package com.tohant.om2d.storage.database;

import com.badlogic.gdx.Gdx;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.tohant.om2d.model.entity.CellEntity;
import com.tohant.om2d.model.entity.ProgressEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class CellDao extends BaseDaoImpl<CellEntity, UUID> {

    private static CellDao instance;

    public CellDao() throws SQLException {
        super(SQLiteDatabaseHelper.getInstance().getConnectionSource(), CellEntity.class);
    }

    public static CellDao getInstance() {
        if (instance == null) {
            try {
                instance = new CellDao();
            } catch (SQLException e) {
                Gdx.app.error("DAO INSTANTIATION EXCEPTION", "CANNOT INSTANTIATE CELL DAO, CAUSE: "
                        + e.getLocalizedMessage());
            }
        }
        return instance;
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

    public CellEntity queryForActorName(String name) {
        try {
            return super.queryForEq("actor_name", name).stream()
                    .findFirst().orElse(null);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT QUERY FOR ACTOR NAME", "CAUSE: " + e.getLocalizedMessage());
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

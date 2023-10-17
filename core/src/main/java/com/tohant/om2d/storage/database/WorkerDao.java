package com.tohant.om2d.storage.database;

import com.badlogic.gdx.Gdx;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.tohant.om2d.model.entity.WorkerEntity;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class WorkerDao extends BaseDaoImpl<WorkerEntity, UUID> {

    private static WorkerDao instance;

    public WorkerDao() throws SQLException {
        super(SQLiteDatabaseHelper.getInstance().getConnectionSource(), WorkerEntity.class);
    }

    public static WorkerDao getInstance() {
        if (instance == null) {
            try {
                instance = new WorkerDao();
            } catch (SQLException e) {
                Gdx.app.error("DAO INSTANTIATION EXCEPTION", "CANNOT INSTANTIATE WORKER DAO, CAUSE: "
                        + e.getLocalizedMessage());
            }
        }
        return instance;
    }

    @Override
    public WorkerEntity queryForId(UUID s) {
        try {
            return super.queryForId(s);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT QUERY FOR ID", "CAUSE: " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public List<WorkerEntity> queryForAll() {
        try {
            return super.queryForAll();
        } catch (SQLException e) {
            Gdx.app.error("CANNOT QUERY FOR ALL", "CAUSE: " + e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public int create(WorkerEntity data) {
        try {
            return super.create(data);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT CREATE", "CAUSE: " + e.getLocalizedMessage());
            return 0;
        }
    }

    @Override
    public int create(Collection<WorkerEntity> datas) {
        try {
            return super.create(datas);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT CREATE", "CAUSE: " + e.getLocalizedMessage());
            return 0;
        }
    }

    @Override
    public int deleteIds(Collection<UUID> uuids) {
        try {
            return super.deleteIds(uuids);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT DELETE IDS", "CAUSE: " + e.getLocalizedMessage());
            return 0;
        }
    }

}

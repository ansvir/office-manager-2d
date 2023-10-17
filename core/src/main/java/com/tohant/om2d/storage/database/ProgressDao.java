package com.tohant.om2d.storage.database;

import com.badlogic.gdx.Gdx;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.tohant.om2d.model.entity.CompanyEntity;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.service.ServiceUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static com.tohant.om2d.service.ServiceUtil.*;

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
            int created = super.create(data);
            if (created <= 0) {
                throw new SQLException("No records were created.");
            }
            data.setActorName(data.getId().toString());
            CompanyEntity companyEntity = data.getCompanyEntity();
            companyEntity.setActorName(getCompanyActorId(companyEntity.getId().toString()));
            companyEntity.getOfficeEntities().forEach(o -> {
                o.setActorName(getOfficeActorId(o.getId().toString(), companyEntity.getActorName()));
                o.getLevelEntities().forEach(l -> {
                    l.setActorName(getGridActorId((int) l.getLevel(), o.getActorName()));
                    l.getCellEntities().forEach(c -> {
                        c.setActorName(getCellActorId(c.getX(), c.getY(), l.getActorName()));
                        c.getRoomEntity().setActorName(getRoomActorId(c.getRoomEntity().getType(), c.getActorName()));
                    });
                });
            });
        } catch (SQLException e) {
            Gdx.app.error("CANNOT CREATE", "CAUSE: " + e.getLocalizedMessage());
            return 0;
        }
    }

    public int update(ProgressEntity data) {
        try {
            return super.update(data);
        } catch (SQLException e) {
            Gdx.app.error("CANNOT UPDATE", "CAUSE: " + e.getLocalizedMessage());
            return 0;
        }
    }

}

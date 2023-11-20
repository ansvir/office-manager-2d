package com.tohant.om2d.storage.database;

import com.j256.ormlite.dao.BaseDaoImpl;

import java.sql.SQLException;
import java.util.UUID;

public abstract class AbstractBaseDao<T> extends BaseDaoImpl<T, UUID> {

    public AbstractBaseDao(SQLiteDatabaseHelper helper, Class<T> type) throws SQLException {
        super(helper.getConnectionSource(), type);
    }

}

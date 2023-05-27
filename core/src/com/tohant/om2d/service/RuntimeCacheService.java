package com.tohant.om2d.service;

import com.tohant.om2d.storage.RuntimeCacheImpl;

import static com.tohant.om2d.actor.constant.Constant.GRID_HEIGHT;
import static com.tohant.om2d.actor.constant.Constant.GRID_WIDTH;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.GRID;
import static com.tohant.om2d.storage.Cache.*;

public class RuntimeCacheService {
    private static RuntimeCacheService instance;

    private final RuntimeCacheImpl runtimeCache;
    
    private RuntimeCacheService() {
        this.runtimeCache = RuntimeCacheImpl.getInstance();
        this.runtimeCache.setValue(CURRENT_LEVEL, 0L);
        this.runtimeCache.setValue(CURRENT_OFFICE_CELLS_WIDTH, GRID_WIDTH);
        this.runtimeCache.setValue(CURRENT_OFFICE_CELLS_HEIGHT, GRID_HEIGHT);
        this.runtimeCache.setValue(CURRENT_ROOM_TYPE, null);
        this.runtimeCache.setValue(CURRENT_BUDGET, 10000.0f);
        this.runtimeCache.setValue(CURRENT_TIME, "01/01/0001");
        this.runtimeCache.setValue(OFFICES_AMOUNT, 0L);
        this.runtimeCache.setValue(HALLS_AMOUNT, 0L);
        this.runtimeCache.setValue(SECURITY_AMOUNT, 0L);
        this.runtimeCache.setValue(CLEANING_AMOUNT, 0L);
        this.runtimeCache.setValue(CAFFE_AMOUNT, 0L);
        this.runtimeCache.setValue(ELEVATOR_AMOUNT, 0L);
        this.runtimeCache.setValue(IS_PAYDAY, false);
        this.runtimeCache.setValue(CURRENT_ROOM, null);
        this.runtimeCache.setValue(TOTAL_COSTS, 0.0f);
        this.runtimeCache.setValue(TOTAL_INCOMES, 0.0f);
        this.runtimeCache.setValue(TOTAL_SALARIES, 0.0f);
        this.runtimeCache.setValue(TOTAL_WORKERS, 0L);
        this.runtimeCache.setValue(TOTAL_ADMIN_STAFF, 0L);
        this.runtimeCache.setValue(TOTAL_CLEANING_STAFF, 0L);
        this.runtimeCache.setValue(TOTAL_SECURITY_STAFF, 0L);
        this.runtimeCache.setValue(TOTAL_CAFFE_STAFF, 0L);
    }

    public static RuntimeCacheService getInstance() {
        if (instance == null) {
            instance = new RuntimeCacheService();
        }
        return instance;
    }

    public void setValue(String key, String value) {
        this.runtimeCache.setValue(key, value);
    }

    public String getValue(String key) {
        return (String) runtimeCache.getValue(key);
    }

    public void setFloat(String key, float value) {
        this.runtimeCache.setValue(key, value);
    }

    public float getFloat(String key) {
        return (float) runtimeCache.getValue(key);
    }

    public void setLong(String key, long value) {
        this.runtimeCache.setValue(key, value);
    }

    public long getLong(String key) {
        return (long) this.runtimeCache.getValue(key);
    }

    public void setBoolean(String key, boolean value) {
        this.runtimeCache.setValue(key, value);
    }

    public boolean getBoolean(String key) {
        return (boolean) this.runtimeCache.getValue(key);
    }

}

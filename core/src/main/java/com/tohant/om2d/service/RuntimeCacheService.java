package com.tohant.om2d.service;

import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.storage.RuntimeCacheImpl;

import static com.tohant.om2d.actor.constant.Constant.GRID_HEIGHT;
import static com.tohant.om2d.actor.constant.Constant.GRID_WIDTH;
import static com.tohant.om2d.storage.Cache.*;

public class RuntimeCacheService {
    private static RuntimeCacheService instance;

    private final RuntimeCacheImpl runtimeCache;
    
    private RuntimeCacheService() {
        this.runtimeCache = RuntimeCacheImpl.getInstance();
        this.setValue(CURRENT_PROGRESS_ID, "");
        this.setValue(COMPANY_NAME, "");
        this.setObject(BUILD_TASKS, new Array<>());
        this.setBoolean(READY_TO_START, false);
        this.setObject(COMPANIES, new Array<>());
        this.setObject(MENU_UI_ACTORS, new Array<>());
        this.setObject(UI_ACTORS, new Array<>());
        this.setObject(WORKERS, new Array<>());
        this.setObject(GAME_EXCEPTION, new Array<>());
        this.setLong(CURRENT_OFFICE_CELLS_WIDTH, GRID_WIDTH);
        this.setLong(CURRENT_OFFICE_CELLS_HEIGHT, GRID_HEIGHT);
        this.setFloat(CURRENT_BUDGET, 10000.0f);
        this.setValue(CURRENT_TIME, "01/01/0001");
        this.setLong(OFFICES_AMOUNT, 0L);
        this.setLong(HALLS_AMOUNT, 0L);
        this.setLong(SECURITY_AMOUNT, 0L);
        this.setLong(CLEANING_AMOUNT, 0L);
        this.setLong(CAFFE_AMOUNT, 0L);
        this.setLong(ELEVATOR_AMOUNT, 0L);
        this.setBoolean(IS_PAYDAY, false);
        this.setFloat(TOTAL_COSTS, 0.0f);
        this.setFloat(TOTAL_INCOMES, 0.0f);
        this.setFloat(TOTAL_SALARIES, 0.0f);
        this.setLong(TOTAL_WORKERS, 0L);
        this.setLong(TOTAL_ADMIN_STAFF, 0L);
        this.setLong(TOTAL_CLEANING_STAFF, 0L);
        this.setLong(TOTAL_SECURITY_STAFF, 0L);
        this.setLong(TOTAL_CAFFE_STAFF, 0L);
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

    public void setObject(String key, Object o) {
        this.runtimeCache.setValue(key, o);
    }

    public Object getObject(String key) {
        return this.runtimeCache.getValue(key);
    }

}

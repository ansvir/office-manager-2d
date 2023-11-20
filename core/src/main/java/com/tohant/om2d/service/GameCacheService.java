package com.tohant.om2d.service;

import com.badlogic.gdx.utils.Array;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.di.annotation.PostConstruct;
import com.tohant.om2d.storage.cache.GameCache;

import static com.tohant.om2d.actor.constant.Constant.GRID_HEIGHT;
import static com.tohant.om2d.actor.constant.Constant.GRID_WIDTH;

@Component
public class GameCacheService implements GameCache {

    private Cache<String, Object> cache;

    @PostConstruct
    public void init() {
        cache = Caffeine.newBuilder().build();
        initProperties();
    }
    
    private void initProperties() {
        this.setValue(CURRENT_PROGRESS_ID, "");
        this.setValue(CURRENT_OFFICE_ID, "");
        this.setValue(CURRENT_LEVEL_ID, "");
        this.setValue(CURRENT_REGION, "");
        this.setValue(COMPANY_NAME, "");
        this.setObject(BUILD_TASKS, new Array<>());
        this.setBoolean(READY_TO_START, false);
        this.setBoolean(NEW_GAME, false);
        this.setObject(COMPANIES, new Array<>());
        this.setObject(MENU_UI_ACTORS, new Array<>());
        this.setObject(GAME_ACTORS, new Array<>());
        this.setObject(WORKERS, new Array<>());
        this.setObject(GAME_EXCEPTION, new Array<>());
        this.setLong(CURRENT_OFFICE_CELLS_WIDTH, GRID_WIDTH);
        this.setLong(CURRENT_OFFICE_CELLS_HEIGHT, GRID_HEIGHT);
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

    @Override
    public Cache<String, Object> getCache() {
        return cache;
    }

    @Override
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object get(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void setValue(String key, String value) {
        put(key, value);
    }

    @Override
    public String getValue(String key) {
        return (String) get(key);
    }

    @Override
    public void setFloat(String key, float value) {
        put(key, value);
    }

    @Override
    public Float getFloat(String key) {
        return (Float) get(key);
    }

    @Override
    public void setLong(String key, long value) {
        put(key, value);
    }

    @Override
    public long getLong(String key) {
        return (long) get(key);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        put(key, value);
    }

    @Override
    public boolean getBoolean(String key) {
        return (boolean) get(key);
    }

    @Override
    public void setObject(String key, Object o) {
        put(key, o);
    }

    @Override
    public Object getObject(String key) {
        return get(key);
    }

}

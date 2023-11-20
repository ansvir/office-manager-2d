package com.tohant.om2d.storage.cache;

import com.github.benmanes.caffeine.cache.Cache;

public interface GameCache {

    String OFFICE_INFO = "OFFICE_INFO";
    String COMPANIES = "COMPANIES";
    String CURRENT_OBJECT_CELL = "CURRENT_OBJECT_CELL";
    String CURRENT_SCREEN = "CURR_SCREEN";
    String CURRENT_ITEM = "CURRENT_ITEM";
    String CURRENT_PROGRESS_ID = "CURR_PROGRESS_ID";
    String CURRENT_OFFICE_ID = "CURR_OFFICE_ID";
    String CURRENT_LEVEL_ID = "CURR_LEVEL_ID";
    String GAME_ACTORS = "UI_ACTORS";
    String MENU_UI_ACTORS = "MENU_UI_ACTORS";
    String WORKERS = "WORKERS";
    String GAME_EXCEPTION = "GAME_EXCEPTION";
    String CURRENT_OFFICE_CELLS_WIDTH = "CURR_OFFICE_CELLS_WIDTH";
    String CURRENT_OFFICE_CELLS_HEIGHT = "CURR_OFFICE_CELLS_HEIGHT";
    String CURRENT_ROOM_TYPE = "CURR_ROOM_TYPE";
    String CURRENT_BUDGET = "CURR_BUDGET";
    String CURRENT_DELTA_TIME = "CURR_DELTA_TIME";
    String CURRENT_TIME = "CURR_TIME";
    String OFFICES_AMOUNT = "OFFICES_AM";
    String HALLS_AMOUNT = "HALLS_AM";
    String SECURITY_AMOUNT = "SECURITY_AM";
    String CLEANING_AMOUNT = "CLEANING_AM";
    String CAFFE_AMOUNT = "CAFFE_AM";
    String ELEVATOR_AMOUNT = "ELEVATOR_AM";
    String IS_PAYDAY = "IS_PAYDAY";
    String CURRENT_CELL = "CURR_CELL";
    String READY_TO_START = "READY_TO_START";
    String NEW_GAME = "NEW_GAME";
    String BUILD_TASKS = "BUILD_TASKS";
    String CURRENT_REGION = "CURR_REGION";
    String CURRENT_CHOSEN_REGION = "CURR_CHOSEN_REGION";
    String COMPANY_NAME = "COMPANY_NAME";
    String TOTAL_COSTS = "TOTAL_COSTS";
    String TOTAL_SALARIES = "TOTAL_SALAR";
    String TOTAL_INCOMES = "TOTAL_INC";
    String TOTAL_SECURITY_STAFF = "TOTAL_SEC_STAFF";
    String TOTAL_ADMIN_STAFF = "TOTAL_ADMIN_STAFF";
    String TOTAL_CLEANING_STAFF = "TOTAL_CLEANING_STAFF";
    String TOTAL_WORKERS = "TOTAL_WORKERS";
    String TOTAL_CAFFE_STAFF = "TOTAL_CAFFE_STAFF";

    Cache<String, Object> getCache();

    void put(String key, Object value);

    Object get(String key);

    void setValue(String key, String value);

    String getValue(String key);

    void setFloat(String key, float value);

    Float getFloat(String key);

    void setLong(String key, long value);

    long getLong(String key);

    void setBoolean(String key, boolean value);

    boolean getBoolean(String key);

    void setObject(String key, Object o);

    Object getObject(String key);


}

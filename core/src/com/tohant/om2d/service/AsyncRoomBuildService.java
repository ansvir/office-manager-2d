package com.tohant.om2d.service;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.OfficeRoom;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.storage.CacheProxy;

import static com.tohant.om2d.actor.constant.Constant.GRID_HEIGHT;
import static com.tohant.om2d.actor.constant.Constant.GRID_WIDTH;
import static com.tohant.om2d.storage.CacheImpl.*;
import static com.tohant.om2d.storage.CacheImpl.TOTAL_ADMIN_STAFF;

public class AsyncRoomBuildService {

    private static AsyncRoomBuildService instance;
    private final AsyncExecutor asyncExecutor;
    private final CacheService cacheService;
    private Array<TimeLineTask<Room>> tasks;

    private AsyncRoomBuildService() {
        asyncExecutor = new AsyncExecutor(GRID_WIDTH * GRID_HEIGHT);
        cacheService = CacheService.getInstance();
        this.tasks = Array.with();
    }

    public synchronized static AsyncRoomBuildService getInstance() {
        if (instance == null) {
            instance = new AsyncRoomBuildService();
        }
        return instance;
    }

    public synchronized AsyncResult<Room> submit(Room room) {
        TimeLineTask<Room> task = new TimeLineTask<>(room.getRoomInfo().getId(), 500L, room,
                (d) -> d.compareTo(room.getRoomInfo().getBuildTime()) >= 0, () -> {
            if (room instanceof OfficeRoom) {
                cacheService.setFloat(TOTAL_INCOMES, cacheService.getFloat(TOTAL_INCOMES) - 1000.0f);
            }
            if (!room.getRoomInfo().getStaff().isEmpty() && room.getRoomInfo().getStaff().get(0).getType() != null) {
                Staff employee = room.getRoomInfo().getStaff().get(0);
                Staff.Type type = employee.getType();
                float salary = employee.getSalary();
                setEmployeesAmountByType(type,
                        getEmployeesAmountByType(type) - room.getRoomInfo().getStaff().size);
                cacheService.setFloat(TOTAL_SALARIES, cacheService.getFloat(TOTAL_SALARIES)
                        - room.getRoomInfo().getStaff().size * salary);
            }
        });
        this.tasks.add(task);
        return asyncExecutor.submit(task);
    }

    private long getEmployeesAmountByType(Staff.Type type) {
        switch (type) {
            case SECURITY: return cacheService.getLong(TOTAL_SECURITY_STAFF);
            case WORKER: return cacheService.getLong(TOTAL_WORKERS);
            case CLEANING: return cacheService.getLong(TOTAL_CLEANING_STAFF);
            case ADMINISTRATION: return cacheService.getLong(TOTAL_ADMIN_STAFF);
            default: return -1L;
        }
    }

    private void setEmployeesAmountByType(Staff.Type type, long amount) {
        switch (type) {
            case SECURITY:
                cacheService.setLong(TOTAL_SECURITY_STAFF, amount);
                break;
            case CLEANING:
                cacheService.setLong(TOTAL_CLEANING_STAFF, amount);
                break;
            case WORKER:
                cacheService.setLong(TOTAL_WORKERS, amount);
                break;
            case ADMINISTRATION:
                cacheService.setLong(TOTAL_ADMIN_STAFF, amount);
                break;
            default:
                break;
        }
    }

    public Array<TimeLineTask<Room>> getTasks() {
        return tasks;
    }
}

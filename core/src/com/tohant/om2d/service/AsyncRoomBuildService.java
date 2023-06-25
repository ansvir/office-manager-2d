package com.tohant.om2d.service;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.OfficeRoom;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.model.task.TimeLineTask;

import java.util.concurrent.CompletableFuture;

import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.service.ServiceUtil.getEmployeesAmountByType;
import static com.tohant.om2d.service.ServiceUtil.setEmployeesAmountByType;
import static com.tohant.om2d.storage.Cache.*;

public class AsyncRoomBuildService {

    private static AsyncRoomBuildService instance;
    private final AsyncExecutor asyncExecutor;
    private final RuntimeCacheService cacheService;
    private final Array<TimeLineTask<Room>> tasks;

    private AsyncRoomBuildService() {
        asyncExecutor = new AsyncExecutor(GRID_WIDTH * GRID_HEIGHT);
        cacheService = RuntimeCacheService.getInstance();
        this.tasks = Array.with();
    }

    public synchronized static AsyncRoomBuildService getInstance() {
        if (instance == null) {
            instance = new AsyncRoomBuildService();
        }
        return instance;
    }

    public synchronized CompletableFuture<Room> submitBuild(Cell cell, Room room) {
        TimeLineTask<Room> task = new TimeLineTask<>(room.getRoomInfo().getId(), DAY_WAIT_TIME_MILLIS, room,
                (d) -> d.compareTo(room.getRoomInfo().getBuildTime()) >= 0, () -> {}, () -> {
            String staffTypeString = room.getType() == Room.Type.SECURITY ? TOTAL_SECURITY_STAFF
                    : room.getType() == Room.Type.CLEANING ? TOTAL_CLEANING_STAFF
                    : room.getType() == Room.Type.OFFICE ? TOTAL_WORKERS
                    : room.getType() == Room.Type.CAFFE ? TOTAL_CAFFE_STAFF : null;
            if (room instanceof OfficeRoom) {
                cacheService.setFloat(TOTAL_INCOMES, cacheService.getFloat(TOTAL_INCOMES) + 100.0f * room.getRoomInfo().getStaff().size);
            }
            Staff.Type staffType = null;
            if (staffTypeString != null) {
                staffType = staffTypeString.equals(TOTAL_SECURITY_STAFF) ? Staff.Type.SECURITY
                        : staffTypeString.equals(TOTAL_CLEANING_STAFF) ? Staff.Type.CLEANING
                        : staffTypeString.equals(TOTAL_CAFFE_STAFF) ? Staff.Type.CAFFE
                        : Staff.Type.WORKER;
            }
            if (staffType != null) {
                setEmployeesAmountByType(staffType,
                        getEmployeesAmountByType(staffType) + room.getRoomInfo().getStaff().size);
                cacheService.setFloat(TOTAL_SALARIES, cacheService.getFloat(TOTAL_SALARIES)
                        + room.getRoomInfo().getStaff().size * staffType.getSalary());
            }
            cell.setDetails(room);
        });
        this.tasks.add(task);
        asyncExecutor.submit(task);
        return task;
    }

    /*
    public synchronized CompletableFuture<Room> submitDestroy(Room room) {
        TimeLineTask<Room> task = new TimeLineTask<>(room.getRoomInfo().getId(), DAY_WAIT_TIME_MILLIS, room,
                (d) -> d.getDays() >= 1, () -> {});
        this.tasks.add(task);
        asyncExecutor.submit(task);
        return task;
    }
     */

    public Array<TimeLineTask<Room>> getTasks() {
        return tasks;
    }

}

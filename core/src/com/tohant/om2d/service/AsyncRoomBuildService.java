package com.tohant.om2d.service;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.Office;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.OfficeRoom;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.model.entity.ResidentEntity;
import com.tohant.om2d.model.entity.WorkerEntity;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.storage.database.ResidentJsonDatabase;
import com.tohant.om2d.storage.database.WorkerJsonDatabase;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.service.ServiceUtil.*;
import static com.tohant.om2d.storage.Cache.*;

public class AsyncRoomBuildService {

    private static AsyncRoomBuildService instance;
    private final AsyncExecutor asyncExecutor;
    private final RuntimeCacheService cacheService;

    private AsyncRoomBuildService() {
        asyncExecutor = new AsyncExecutor(GRID_WIDTH * GRID_HEIGHT);
        cacheService = RuntimeCacheService.getInstance();
    }

    public synchronized static AsyncRoomBuildService getInstance() {
        if (instance == null) {
            instance = new AsyncRoomBuildService();
        }
        return instance;
    }

    public synchronized RoomBuildingModel submitBuild(Cell cell, Room room) {
        TimeLineTask<Room> task = new TimeLineTask<>(room.getRoomInfo().getId(), DAY_WAIT_TIME_MILLIS, room,
                (d) -> d.compareTo(room.getRoomInfo().getBuildTime()) >= 0, () -> {
            cell.getChildren().iterator().forEach(c -> {
                if (c instanceof ProgressBar) {
                    Array<RoomBuildingModel> buildingModels = (Array<RoomBuildingModel>) RuntimeCacheService.getInstance().getObject(BUILD_TASKS);
                    for (RoomBuildingModel model : buildingModels.iterator()) {
                        if (model.getRoomInfo().getId().equals(room.getRoomInfo().getId())) {
                            ((ProgressBar) c).setValue(model.getTimeLineTask().getDate().getDays());
                        }
                    }
                }
            });
        }, () -> {
            String staffTypeString = room.getType() == Room.Type.SECURITY ? TOTAL_SECURITY_STAFF
                    : room.getType() == Room.Type.CLEANING ? TOTAL_CLEANING_STAFF
                    : room.getType() == Room.Type.OFFICE ? TOTAL_WORKERS
                    : room.getType() == Room.Type.CAFFE ? TOTAL_CAFFE_STAFF : null;
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
            Array<WorkerEntity> workers = new Array<>(Arrays.stream(room.getRoomInfo().getStaff().toArray(Staff.class))
                    .map(s -> new WorkerEntity(UUID.randomUUID().toString(), s.getFullName(), s.getManInfo().getMood(), s.getType().name(), s.getSalary()))
                    .toArray(WorkerEntity[]::new));
            Array<String> staffIds = new Array<>(Arrays.stream(room.getRoomInfo().getStaff().toArray(Staff.class))
                    .map(Actor::getName).toArray(String[]::new));
            WorkerJsonDatabase workerJsonDatabase = WorkerJsonDatabase.getInstance();
            workerJsonDatabase.saveAll(workers);
            if (room instanceof OfficeRoom) {
                cacheService.setFloat(TOTAL_INCOMES, cacheService.getFloat(TOTAL_INCOMES) + 100.0f * room.getRoomInfo().getStaff().size);
                ResidentEntity residentEntity = new ResidentEntity(UUID.randomUUID().toString(), buildRandomCompanyName(), staffIds);
                ResidentJsonDatabase residentJsonDatabase = ResidentJsonDatabase.getInstance();
                residentJsonDatabase.save(residentEntity);
            }
            addObjectCellsAndStaff(cell, room);
            Array<RoomBuildingModel> buildingModels = (Array<RoomBuildingModel>) cacheService.getObject(BUILD_TASKS);
            for (int i = 0; i < buildingModels.size; i++) {
                if (buildingModels.get(i).getRoomInfo().getId().equals(room.getRoomInfo().getId())) {
                    buildingModels.removeIndex(i);
                }
            }
            Array<Actor> cellChildren = cell.getChildren();
            for (int i = 0; i < cellChildren.size; i++) {
                if (cellChildren.get(i) instanceof ProgressBar) {
                    cell.getChildren().get(i).remove();
                    break;
                }
            }
        });
        return new RoomBuildingModel(task, asyncExecutor.submit(task), room.getRoomInfo());
    }

    private void addObjectCellsAndStaff(Cell cell, Room room) {
        getObjectCells(cell, room.getType()).iterator().forEach(c -> c.iterator().forEach(cell::addActor));
        UiActorService uiActorService = UiActorService.getInstance();
        RuntimeCacheService cache = RuntimeCacheService.getInstance();
        String currentCompanyId = cache.getValue(CURRENT_COMPANY_ID);
        String currentOfficeId = cache.getValue(CURRENT_OFFICE_ID);
        String officeId = getOfficeActorId(currentOfficeId, currentCompanyId);
        Office office = (Office) uiActorService.getActorById(officeId);
        room.getRoomInfo().getStaff().forEach(office::addActor);
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

}

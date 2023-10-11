package com.tohant.om2d.service;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.OfficeRoom;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.model.entity.*;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.storage.cache.Cache;
import com.tohant.om2d.storage.database.CellDao;
import com.tohant.om2d.storage.database.ProgressDao;
import com.tohant.om2d.storage.database.ResidentDao;
import com.tohant.om2d.storage.database.RoomDao;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.service.ServiceUtil.*;

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
                (d) -> d.compareTo(room.getRoomInfo().getBuildTime()) >= 0, () -> cell.getChildren().iterator().forEach(c -> {
                    if (c instanceof ProgressBar) {
                        Array<RoomBuildingModel> buildingModels = (Array<RoomBuildingModel>) RuntimeCacheService.getInstance().getObject(Cache.BUILD_TASKS);
                        for (RoomBuildingModel model : buildingModels.iterator()) {
                            if (model.getRoomInfo().getId().equals(room.getRoomInfo().getId())) {
                                ((ProgressBar) c).setValue(model.getTimeLineTask().getDate().getDays());
                            }
                        }
                    }
                }), () -> {
            String staffTypeString = room.getType() == Room.Type.SECURITY ? Cache.TOTAL_SECURITY_STAFF
                    : room.getType() == Room.Type.CLEANING ? Cache.TOTAL_CLEANING_STAFF
                    : room.getType() == Room.Type.OFFICE ? Cache.TOTAL_WORKERS
                    : room.getType() == Room.Type.CAFFE ? Cache.TOTAL_CAFFE_STAFF : null;
            Staff.Type staffType = null;
            if (staffTypeString != null) {
                staffType = staffTypeString.equals(Cache.TOTAL_SECURITY_STAFF) ? Staff.Type.SECURITY
                        : staffTypeString.equals(Cache.TOTAL_CLEANING_STAFF) ? Staff.Type.CLEANING
                        : staffTypeString.equals(Cache.TOTAL_CAFFE_STAFF) ? Staff.Type.CAFFE
                        : Staff.Type.WORKER;
            }
            if (staffType != null) {
                setEmployeesAmountByType(staffType,
                        getEmployeesAmountByType(staffType) + room.getRoomInfo().getStaff().size);
                cacheService.setFloat(Cache.TOTAL_SALARIES, cacheService.getFloat(Cache.TOTAL_SALARIES)
                        + room.getRoomInfo().getStaff().size * staffType.getSalary());
            }
            List<WorkerEntity> workers = new LinkedList<>();
            Array<Staff> staff = room.getRoomInfo().getStaff();
            for (int i = 0; i < staff.size; i++) {
                workers.add(new WorkerEntity(getStaffActorId(staff.get(i), i, room.getRoomInfo().getId()),
                        staff.get(i).getFullName(), staff.get(i).getManInfo().getMood(),
                        staff.get(i).getType().name(), staff.get(i).getSalary()));
            }
            ProgressEntity progressEntity = ProgressDao.getInstance().queryForId(UUID.fromString(cacheService.getValue(Cache.CURRENT_PROGRESS_ID)));
            OfficeEntity officeEntity = progressEntity.getOfficeEntity();
            RoomEntity roomEntity;
            if (room instanceof OfficeRoom) {
                cacheService.setFloat(Cache.TOTAL_INCOMES, cacheService.getFloat(Cache.TOTAL_INCOMES) + 100.0f * room.getRoomInfo().getStaff().size);
                ResidentEntity residentEntity = new ResidentEntity(getResidentActorId(UUID.randomUUID().toString(), officeEntity.getActorName()),
                        buildRandomCompanyName(), officeEntity);
                ResidentDao.getInstance().create(residentEntity);
                roomEntity = new RoomEntity(room.getRoomInfo().getId(), room.getType().name(),
                        room.getRoomInfo().getPrice(), room.getRoomInfo().getCost(), room.getRoomInfo().getNumber(), (int) room.getRoomInfo().getBuildTime().getDays(),
                        (int) room.getRoomInfo().getBuildTime().getMonth(), (int) room.getRoomInfo().getBuildTime().getYears(), workers, residentEntity);
            } else {
                roomEntity = new RoomEntity(room.getRoomInfo().getId(), room.getType().name(),
                        room.getRoomInfo().getPrice(), room.getRoomInfo().getCost(), room.getRoomInfo().getNumber(), (int) room.getRoomInfo().getBuildTime().getDays(),
                        (int) room.getRoomInfo().getBuildTime().getMonth(), (int) room.getRoomInfo().getBuildTime().getYears(), workers);
            }
            cell.addActor(room);
            addEmptyObjectCells(cell, room);
            Array<RoomBuildingModel> buildingModels = (Array<RoomBuildingModel>) cacheService.getObject(Cache.BUILD_TASKS);
            for (int i = 0; i < buildingModels.size; i++) {
                if (buildingModels.get(i).getRoomInfo().getId().equals(room.getRoomInfo().getId())) {
                    buildingModels.removeIndex(i);
                }
            }
            Array<Actor> cellChildren = cell.getChildren();
            for (int i = 0; i < cellChildren.size; i++) {
                if (cellChildren.get(i) instanceof ProgressBar) {
                    cellChildren.get(i).remove();
                    break;
                }
            }
            RoomDao.getInstance().create(roomEntity);
            CellEntity cellEntity = CellDao.getInstance().queryForActorName(cell.getName());
            cellEntity.setRoomEntity(roomEntity);
            CellDao.getInstance().update(cellEntity);
        });
        return new RoomBuildingModel(task, asyncExecutor.submit(task), room.getRoomInfo());
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

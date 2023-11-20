package com.tohant.om2d.service;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.OfficeRoom;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.di.annotation.PostConstruct;
import com.tohant.om2d.model.entity.*;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.storage.cache.GameCache;
import com.tohant.om2d.storage.database.*;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.service.CommonService.*;
import static com.tohant.om2d.storage.cache.GameCache.BUILD_TASKS;

@Component
@RequiredArgsConstructor
public class AsyncRoomBuildService {

    private final GameCache gameCache;
    private final CommonService commonService;
    private final CellDao cellDao;
    private final RoomDao roomDao;
    private final ResidentDao residentDao;
    private final WorkerDao workerDao;
    private final OfficeDao officeDao;

    private AsyncExecutor asyncExecutor;

    @PostConstruct
    public void init() {
        asyncExecutor = new AsyncExecutor(GRID_WIDTH * GRID_HEIGHT);
    }

    public synchronized RoomBuildingModel submitBuild(Cell cell, Room room) {
        TimeLineTask<Room> task = new TimeLineTask<>(room.getRoomInfo().getId(), DAY_WAIT_TIME_MILLIS, room,
                (d) -> d.compareTo(room.getRoomInfo().getBuildTime()) >= 0, () -> cell.getChildren().iterator().forEach(c -> {
                    if (c instanceof ProgressBar) {
                        Array<RoomBuildingModel> buildingModels = (Array<RoomBuildingModel>) gameCache.getObject(BUILD_TASKS);
                        for (RoomBuildingModel model : buildingModels.iterator()) {
                            if (model.getRoomInfo().getId().equals(room.getRoomInfo().getId())) {
                                ((ProgressBar) c).setValue(model.getTimeLineTask().getDate().getDays());
                            }
                        }
                    }
                    room.getRoomInfo().setUnderConstruction(true);
                }), () -> {
            String staffTypeString = room.getType() == Room.Type.SECURITY ? GameCache.TOTAL_SECURITY_STAFF
                    : room.getType() == Room.Type.CLEANING ? GameCache.TOTAL_CLEANING_STAFF
                    : room.getType() == Room.Type.OFFICE ? GameCache.TOTAL_WORKERS
                    : room.getType() == Room.Type.CAFFE ? GameCache.TOTAL_CAFFE_STAFF : null;
            Staff.Type staffType = null;
            if (staffTypeString != null) {
                staffType = staffTypeString.equals(GameCache.TOTAL_SECURITY_STAFF) ? Staff.Type.SECURITY
                        : staffTypeString.equals(GameCache.TOTAL_CLEANING_STAFF) ? Staff.Type.CLEANING
                        : staffTypeString.equals(GameCache.TOTAL_CAFFE_STAFF) ? Staff.Type.CAFFE
                        : Staff.Type.WORKER;
            }
            if (staffType != null) {
                commonService.setEmployeesAmountByType(staffType,
                        commonService.getEmployeesAmountByType(staffType) + room.getRoomInfo().getStaff().size);
                gameCache.setFloat(GameCache.TOTAL_SALARIES, gameCache.getFloat(GameCache.TOTAL_SALARIES)
                        + room.getRoomInfo().getStaff().size * staffType.getSalary());
            }
            List<WorkerEntity> workers = new LinkedList<>();
            Array<Staff> staff = room.getRoomInfo().getStaff();
            for (int i = 0; i < staff.size; i++) {
                workers.add(new WorkerEntity(
                        staff.get(i).getFullName(), staff.get(i).getManInfo().getMood(),
                        staff.get(i).getType().name(), staff.get(i).getSalary()));
            }
            OfficeEntity officeEntity = officeDao.queryForId(UUID.fromString(gameCache.getValue(GameCache.CURRENT_OFFICE_ID)));
            RoomEntity roomEntity;
            if (room instanceof OfficeRoom) {
                gameCache.setFloat(GameCache.TOTAL_INCOMES, gameCache.getFloat(GameCache.TOTAL_INCOMES) + 100.0f * room.getRoomInfo().getStaff().size);
                ResidentEntity residentEntity = new ResidentEntity(buildRandomCompanyName(), officeEntity);
                residentDao.create(residentEntity);
                roomEntity = new RoomEntity(room.getType().name(),
                        room.getRoomInfo().getPrice(), room.getRoomInfo().getCost(), room.getRoomInfo().getNumber(), (int) room.getRoomInfo().getBuildTime().getDays(),
                        (int) room.getRoomInfo().getBuildTime().getMonth(), (int) room.getRoomInfo().getBuildTime().getYears(), workers, residentEntity);
            } else {
                roomEntity = new RoomEntity(room.getType().name(), room.getRoomInfo().getPrice(), room.getRoomInfo().getCost(), room.getRoomInfo().getNumber(), (int) room.getRoomInfo().getBuildTime().getDays(),
                        (int) room.getRoomInfo().getBuildTime().getMonth(), (int) room.getRoomInfo().getBuildTime().getYears(), workers);
            }
            cell.addActor(room);
            commonService.addEmptyObjectCells(cell, room);
            Array<RoomBuildingModel> buildingModels = (Array<RoomBuildingModel>) gameCache.getObject(BUILD_TASKS);
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
            roomDao.create(roomEntity);
            room.getRoomInfo().setId(roomEntity.getId().toString());
            room.getRoomInfo().setUnderConstruction(false);
            room.setName(roomEntity.getId().toString());
            CellEntity cellEntity = cellDao.queryForId(UUID.fromString(cell.getName()));
            cellEntity.setRoomEntity(roomEntity);
            cellDao.update(cellEntity);
            workerDao.create(workers);
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

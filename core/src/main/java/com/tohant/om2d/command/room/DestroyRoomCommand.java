package com.tohant.om2d.command.room;

import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.Office;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.command.ui.ForceToggleCommand;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.ServiceUtil;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.storage.Cache;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.model.entity.ProgressEntity;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.storage.database.ProgressJsonDatabase;

import java.util.concurrent.atomic.AtomicReference;

public class DestroyRoomCommand implements Command {

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        RuntimeCacheService cache = RuntimeCacheService.getInstance();
        String cellId = cache.getValue(Cache.CURRENT_CELL);
        Cell currentCell = (Cell) uiActorService.getActorById(cellId);
        if (currentCell != null && !currentCell.isEmpty()) {
            AtomicReference<Room> roomAtomic = new AtomicReference<>();
            currentCell.getChildren().iterator().forEach(c -> {
                if (c instanceof Room) {
                    roomAtomic.set((Room) c);
                }
            });
            Room room = roomAtomic.get();
            if (room != null) {
                if (room.getType() == Room.Type.HALL &&
                        ServiceUtil.checkHallNextToRoomThatHasNoOtherHalls(currentCell)) {
                    throw new GameException(GameException.Code.E300);
                }
                Staff.Type currentStaffType = null;
                float currentStaffTypeSalary = 0.0f;
                switch (room.getType()) {
                    case SECURITY:
                        currentStaffType = Staff.Type.SECURITY;
                        currentStaffTypeSalary = Staff.Type.SECURITY.getSalary();
                        break;
                    case OFFICE:
                        currentStaffType = Staff.Type.WORKER;
                        break;
                    case CLEANING:
                        currentStaffType = Staff.Type.CLEANING;
                        currentStaffTypeSalary = Staff.Type.CLEANING.getSalary();
                        break;
                }
                cache.setFloat(Cache.TOTAL_COSTS, cache.getFloat(Cache.TOTAL_COSTS) - room.getRoomInfo().getCost());
                if (room.getType() == Room.Type.OFFICE) {
                    cache.setFloat(Cache.TOTAL_INCOMES, cache.getFloat(Cache.TOTAL_INCOMES) - 100.0f
                            * room.getRoomInfo().getStaff().size);
                }
                if (currentStaffType != null) {
                    ServiceUtil.setEmployeesAmountByType(currentStaffType,
                            ServiceUtil.getEmployeesAmountByType(currentStaffType)
                                    - room.getRoomInfo().getStaff().size);
                }
                cache.setFloat(Cache.TOTAL_SALARIES, cache.getFloat(Cache.TOTAL_SALARIES)
                        - room.getRoomInfo().getStaff().size * currentStaffTypeSalary);
                ServiceUtil.setRoomsAmountByType(room.getType(),
                        ServiceUtil.getRoomsAmountByType(room.getType()) - 1L);
                Array<RoomBuildingModel> buildingModels = (Array<RoomBuildingModel>) cache.getObject(Cache.BUILD_TASKS);
                AtomicReference<RoomBuildingModel> buildingModelAtomic = new AtomicReference<>();
                buildingModels.iterator().forEach(b -> {
                    if (b.getRoomInfo().getId().equals(room.getRoomInfo().getId())) {
                        buildingModelAtomic.set(b);
                    }
                });
                RoomBuildingModel buildingModel = buildingModelAtomic.get();
                if (buildingModel != null) {
                    if (!buildingModel.getTimeLineTask().isFinished()) {
                        buildingModels.removeValue(buildingModel, false);
                        destroyNonBuiltRoom(currentCell);
                    }
                } else {
                    destroyBuiltRoom(currentCell, room);
                }
            }
            cache.setValue(Cache.CURRENT_CELL, null);
            new ForceToggleCommand(UiActorService.UiComponentConstant.ROOM_INFO_MODAL.name(), false).execute();
            AssetService.getInstance().getDemolishSound().play();
        }
    }

    private void destroyBuiltRoom(Cell cell, Room room) {
        UiActorService uiActorService = UiActorService.getInstance();
        RuntimeCacheService cache = RuntimeCacheService.getInstance();
        ProgressEntity progressEntity = ProgressJsonDatabase.getInstance().getById(cache.getValue(Cache.CURRENT_PROGRESS_ID)).get();
        String currentCompanyId = progressEntity.getCompanyEntity().getActorName();
        String currentOfficeId = progressEntity.getOfficeEntity().getActorName();
        String officeId = ServiceUtil.getOfficeActorId(currentOfficeId, currentCompanyId);
        Office office = (Office) uiActorService.getActorById(officeId);
        room.getRoomInfo().getStaff().forEach(office::removeActor);
        cell.clearChildren();
        cell.setEmpty(true);
    }

    private void destroyNonBuiltRoom(Cell cell) {
        cell.clearChildren();
        cell.setEmpty(true);
    }
}

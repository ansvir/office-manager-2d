package com.tohant.om2d.command.room;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.Grid;
import com.tohant.om2d.actor.Office;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.command.ui.ForceToggleCommand;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

import java.util.concurrent.atomic.AtomicReference;

import static com.tohant.om2d.actor.room.Room.Type.*;
import static com.tohant.om2d.service.ServiceUtil.*;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.ROOM_INFO_MODAL;
import static com.tohant.om2d.storage.Cache.*;

public class DestroyRoomCommand implements Command {

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        RuntimeCacheService cache = RuntimeCacheService.getInstance();
        String cellId = cache.getValue(CURRENT_CELL);
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
                        checkHallNextToRoomThatHasNoOtherHalls(currentCell)) {
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
                cache.setFloat(TOTAL_COSTS, cache.getFloat(TOTAL_COSTS) - room.getRoomInfo().getCost());
                if (room.getType() == OFFICE) {
                    cache.setFloat(TOTAL_INCOMES, cache.getFloat(TOTAL_INCOMES) - 100.0f
                            * room.getRoomInfo().getStaff().size);
                }
                if (currentStaffType != null) {
                    setEmployeesAmountByType(currentStaffType,
                            getEmployeesAmountByType(currentStaffType)
                                    - room.getRoomInfo().getStaff().size);
                }
                cache.setFloat(TOTAL_SALARIES, cache.getFloat(TOTAL_SALARIES)
                        - room.getRoomInfo().getStaff().size * currentStaffTypeSalary);
                setRoomsAmountByType(room.getType(),
                        getRoomsAmountByType(room.getType()) - 1L);
                Array<RoomBuildingModel> buildingModels = (Array<RoomBuildingModel>) cache.getObject(BUILD_TASKS);
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
            cache.setValue(CURRENT_CELL, null);
            new ForceToggleCommand(ROOM_INFO_MODAL.name(), false).execute();
            AssetService.getInstance().getDemolishSound().play();
        }
    }

    private void destroyBuiltRoom(Cell cell, Room room) {
        UiActorService uiActorService = UiActorService.getInstance();
        RuntimeCacheService cache = RuntimeCacheService.getInstance();
        String currentCompanyId = cache.getValue(CURRENT_COMPANY_ID);
        String currentOfficeId = cache.getValue(CURRENT_OFFICE_ID);
        String officeId = getOfficeActorId(currentOfficeId, currentCompanyId);
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

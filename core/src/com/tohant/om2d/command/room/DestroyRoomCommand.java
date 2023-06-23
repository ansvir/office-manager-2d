package com.tohant.om2d.command.room;

import com.tohant.om2d.actor.*;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.command.AbstractCommand;
import com.tohant.om2d.command.ui.ToggleCommand;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

import static com.tohant.om2d.service.ServiceUtil.*;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.*;
import static com.tohant.om2d.storage.CacheImpl.*;

public class DestroyRoomCommand extends AbstractCommand {

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        String roomId = getRuntimeCacheService().getValue(CURRENT_ROOM);
        Cell currentCell = (Cell) uiActorService.getActorById(roomId);
        Grid grid = (Grid) uiActorService.getActorById(GRID.name() + "#" + getRuntimeCacheService().getLong(CURRENT_LEVEL));
        RuntimeCacheService cache = getRuntimeCacheService();
        if (currentCell != null && !currentCell.isEmpty()) {
            if (checkHallNextToRoomThatHasNoOtherHalls(currentCell, grid.getChildren())
                    && currentCell.getRoomModel().getRoomInfo().getType() == Room.Type.HALL) {
                throw new GameException(GameException.Code.E300);
            } else {
                Staff.Type currentStaffType = null;
                float currentStaffTypeSalary = 0.0f;
                switch (currentCell.getRoomModel().getRoomInfo().getType()) {
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
                cache.setFloat(TOTAL_COSTS, cache.getFloat(TOTAL_COSTS) - currentCell.getRoomModel().getRoomInfo().getCost());
                if (currentCell.getRoomModel().getRoomInfo().getType() == Room.Type.OFFICE
                        && currentCell.getRoomModel().getRoom().isDone()) {
                    cache.setFloat(TOTAL_INCOMES, cache.getFloat(TOTAL_INCOMES) - 100.0f
                            * currentCell.getRoomModel().getRoomInfo().getStaff().size);
                }
                if (currentStaffType != null) {
                    setEmployeesAmountByType(currentStaffType,
                            getEmployeesAmountByType(currentStaffType)
                                    - currentCell.getRoomModel().getRoomInfo().getStaff().size);
                }
                if (currentCell.getRoomModel().getRoom().isDone()) {
                    cache.setFloat(TOTAL_SALARIES, cache.getFloat(TOTAL_SALARIES)
                            - currentCell.getRoomModel().getRoomInfo().getStaff().size * currentStaffTypeSalary);
                }
                setRoomsAmountByType(currentCell.getRoomModel().getRoomInfo().getType(),
                        getRoomsAmountByType(currentCell.getRoomModel().getRoomInfo().getType()) - 1L);
                cache.setValue(CURRENT_ROOM, null);
                currentCell.setRoomModel(null);
                new ToggleCommand(ROOM_INFO_MODAL.name()).execute();
                AssetService.getInstance().getDemolishSound().play();
            }
        }
    }
}

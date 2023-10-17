package com.tohant.om2d.command.room;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.Grid;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.command.ui.ForceToggleCommand;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.entity.CellEntity;
import com.tohant.om2d.model.entity.ResidentEntity;
import com.tohant.om2d.model.entity.RoomEntity;
import com.tohant.om2d.model.entity.WorkerEntity;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.storage.cache.Cache;
import com.tohant.om2d.storage.database.CellDao;
import com.tohant.om2d.storage.database.ResidentDao;
import com.tohant.om2d.storage.database.RoomDao;
import com.tohant.om2d.storage.database.WorkerDao;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.tohant.om2d.actor.constant.Constant.CELL_SIZE;
import static com.tohant.om2d.service.ServiceUtil.*;

public class DestroyRoomCommand implements Command {

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        RuntimeCacheService cache = RuntimeCacheService.getInstance();
        String cellId = cache.getValue(Cache.CURRENT_CELL);
        Cell currentCell = (Cell) uiActorService.getActorById(cellId);
        if (currentCell != null && !currentCell.isEmpty()) {
            AtomicReference<Room> roomAtomic = new AtomicReference<>();
            Array<Actor> children = currentCell.getChildren();
            for (int i = 0; i < children.size; i++) {
                if (children.get(i) instanceof Room) {
                    roomAtomic.set((Room) children.get(i));
                }
            }
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
                cache.setFloat(Cache.TOTAL_COSTS, cache.getFloat(Cache.TOTAL_COSTS) - room.getRoomInfo().getCost());
                if (room.getType() == Room.Type.OFFICE) {
                    cache.setFloat(Cache.TOTAL_INCOMES, cache.getFloat(Cache.TOTAL_INCOMES) - 100.0f
                            * room.getRoomInfo().getStaff().size);
                }
                if (currentStaffType != null) {
                    setEmployeesAmountByType(currentStaffType,
                            getEmployeesAmountByType(currentStaffType)
                                    - room.getRoomInfo().getStaff().size);
                }
                cache.setFloat(Cache.TOTAL_SALARIES, cache.getFloat(Cache.TOTAL_SALARIES)
                        - room.getRoomInfo().getStaff().size * currentStaffTypeSalary);
                setRoomsAmountByType(room.getType(),
                        getRoomsAmountByType(room.getType()) - 1L);
                Array<RoomBuildingModel> buildingModels = (Array<RoomBuildingModel>) cache.getObject(Cache.BUILD_TASKS);
                AtomicReference<RoomBuildingModel> buildingModelAtomic = new AtomicReference<>();
                for (int i = 0; i < buildingModels.size; i++) {
                    RoomBuildingModel b = buildingModels.get(i);
                    if (b.getRoomInfo().getId().equals(room.getRoomInfo().getId())) {
                        buildingModelAtomic.set(b);
                    }
                }
                RoomBuildingModel buildingModel = buildingModelAtomic.get();
                if (buildingModel != null) {
                    if (!buildingModel.getTimeLineTask().isFinished()) {
                        buildingModels.removeValue(buildingModel, false);
                        destroyBuildingRoom(currentCell);
                    }
                } else {
                    destroyRoom(currentCell, room);
                }
            }
            cache.setValue(Cache.CURRENT_CELL, null);
            new ForceToggleCommand(UiActorService.UiComponentConstant.ROOM_INFO_MODAL.name(), false).execute();
            AssetService.getInstance().getDemolishSound().play();
        }
    }

    private void destroyRoom(Cell cell, Room room) {
        RoomEntity roomEntity = RoomDao.getInstance().queryForId(UUID.fromString(room.getName()));
        WorkerDao.getInstance().deleteIds(roomEntity.getWorkerEntities().stream().map(WorkerEntity::getId)
                .collect(Collectors.toList()));
        RoomDao.getInstance().deleteById(roomEntity.getId());
        CellEntity cellEntity = CellDao.getInstance().queryForId(UUID.fromString(cell.getName()));
        if (room.getType() == Room.Type.OFFICE) {
            ResidentEntity residentEntity = roomEntity.getResidentEntity();
            ResidentDao.getInstance().deleteById(residentEntity.getId());
        }
        cellEntity.setRoomEntity(null);
        cellEntity.setItems(null);
        CellDao.getInstance().update(cellEntity);
        Cell newCell = new Cell(cellEntity.getId().toString(),
                new ChooseRoomCommand(cellEntity.getId().toString()), cellEntity.getX() * CELL_SIZE,
                cellEntity.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE, null, null);
        Grid grid = (Grid) UiActorService.getInstance().getActorById(RuntimeCacheService.getInstance().getValue(Cache.CURRENT_LEVEL_ID));
        Array<Actor> gridChildren = grid.getChildren();
        for (int i = 0 ; i < gridChildren.size; i++) {
            if (gridChildren.get(i) instanceof Cell
                    && gridChildren.get(i).getName().equals(cell.getName())) {
                gridChildren.set(i, newCell);
                break;
            }
        }
    }

    private void destroyBuildingRoom(Cell cell) {
        CellEntity cellEntity = CellDao.getInstance().queryForId(UUID.fromString(cell.getName()));
        cellEntity.setRoomEntity(null);
        cellEntity.setItems(null);
        CellDao.getInstance().update(cellEntity);
        Cell newCell = new Cell(cellEntity.getId().toString(),
                new ChooseRoomCommand(cellEntity.getId().toString()), cellEntity.getX() * CELL_SIZE,
                cellEntity.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE, null, null);
        Grid grid = (Grid) UiActorService.getInstance().getActorById(RuntimeCacheService.getInstance().getValue(Cache.CURRENT_LEVEL_ID));
        Array<Actor> gridChildren = grid.getChildren();
        for (int i = 0 ; i < gridChildren.size; i++) {
            if (gridChildren.get(i) instanceof Cell
                    && gridChildren.get(i).getName().equals(cell.getName())) {
                gridChildren.set(i, newCell);
                break;
            }
        }
    }
}

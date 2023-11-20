package com.tohant.om2d.command.room;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.Grid;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.entity.CellEntity;
import com.tohant.om2d.model.entity.ResidentEntity;
import com.tohant.om2d.model.entity.RoomEntity;
import com.tohant.om2d.model.entity.WorkerEntity;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.CommonService;
import com.tohant.om2d.service.GameActorFactory;
import com.tohant.om2d.service.GameActorSearchService;
import com.tohant.om2d.storage.cache.GameCache;
import com.tohant.om2d.storage.database.CellDao;
import com.tohant.om2d.storage.database.ResidentDao;
import com.tohant.om2d.storage.database.RoomDao;
import com.tohant.om2d.storage.database.WorkerDao;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.tohant.om2d.storage.cache.GameCache.CURRENT_CELL;

@Component
@RequiredArgsConstructor
public class DestroyRoomCommand implements Command {

    private final GameActorFactory gameActorFactory;
    private final GameActorSearchService gameActorSearchService;
    private final GameCache gameCache;
    private final CellDao cellDao;
    private final CommonService commonService;
    private final WorkerDao workerDao;
    private final RoomDao roomDao;
    private final ResidentDao residentDao;
    
    @Override
    public void execute() {
        String cellId = gameCache.getValue(CURRENT_CELL);
        Cell currentCell = (Cell) gameActorSearchService.getActorById(cellId);
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
                        commonService.checkHallNextToRoomThatHasNoOtherHalls(currentCell, gameActorSearchService)) {
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
                gameCache.setFloat(GameCache.TOTAL_COSTS, gameCache.getFloat(GameCache.TOTAL_COSTS) - room.getRoomInfo().getCost());
                if (room.getType() == Room.Type.OFFICE) {
                    gameCache.setFloat(GameCache.TOTAL_INCOMES, gameCache.getFloat(GameCache.TOTAL_INCOMES) - 100.0f
                            * room.getRoomInfo().getStaff().size);
                }
                if (currentStaffType != null) {
                    commonService.setEmployeesAmountByType(currentStaffType,
                            commonService.getEmployeesAmountByType(currentStaffType)
                                    - room.getRoomInfo().getStaff().size);
                }
                gameCache.setFloat(GameCache.TOTAL_SALARIES, gameCache.getFloat(GameCache.TOTAL_SALARIES)
                        - room.getRoomInfo().getStaff().size * currentStaffTypeSalary);
                commonService.setRoomsAmountByType(room.getType(),
                        commonService.getRoomsAmountByType(room.getType()) - 1L);
                Array<RoomBuildingModel> buildingModels = (Array<RoomBuildingModel>) gameCache.getObject(GameCache.BUILD_TASKS);
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
                        destroyExistingRoom(currentCell);
                    }
                } else {
                    destroyRoom(currentCell, room);
                }
            }
            gameCache.setValue(CURRENT_CELL, null);
            AssetService.DEMOLISH_SOUND.play();
        }
    }

    private void destroyRoom(Cell cell, Room room) {
        RoomEntity roomEntity = roomDao.queryForId(UUID.fromString(room.getName()));
        workerDao.deleteIds(roomEntity.getWorkerEntities().stream().map(WorkerEntity::getId)
                .collect(Collectors.toList()));
        roomDao.deleteById(roomEntity.getId());
        if (room.getType() == Room.Type.OFFICE) {
            ResidentEntity residentEntity = roomEntity.getResidentEntity();
            residentDao.deleteById(residentEntity.getId());
        }
        destroyExistingRoom(cell);
    }

    private void destroyExistingRoom(Cell cell) {
        CellEntity cellEntity = cellDao.queryForId(UUID.fromString(cell.getName()));
        cellEntity.setRoomEntity(null);
        cellEntity.setItems(null);
        cellDao.update(cellEntity);
        Cell newCell = gameActorFactory.createCell(cellEntity);
        Grid grid = (Grid) gameActorSearchService.getActorById(gameCache.getValue(GameCache.CURRENT_LEVEL_ID));
        Array<Actor> gridChildren = grid.getChildren();
        for (int i = 0 ; i < gridChildren.size; i++) {
            if (gridChildren.get(i) instanceof Cell
                    && gridChildren.get(i).getName().equals(cellEntity.getId().toString())) {
                gridChildren.set(i, newCell);
                break;
            }
        }
    }
}

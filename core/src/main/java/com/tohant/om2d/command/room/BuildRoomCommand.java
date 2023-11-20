package com.tohant.om2d.command.room;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.Grid;
import com.tohant.om2d.actor.ToggleActor;
import com.tohant.om2d.actor.man.*;
import com.tohant.om2d.actor.room.*;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.entity.CellEntity;
import com.tohant.om2d.model.entity.LevelEntity;
import com.tohant.om2d.model.office.CompanyInfo;
import com.tohant.om2d.model.room.RoomInfo;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.model.task.TimeLineDate;
import com.tohant.om2d.service.*;
import com.tohant.om2d.storage.cache.GameCache;
import com.tohant.om2d.storage.database.CellDao;
import com.tohant.om2d.storage.database.LevelDao;
import com.tohant.om2d.util.AssetsUtil;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.service.CommonService.*;
import static com.tohant.om2d.service.GameActorFactory.UiComponentConstant.ROOM_INFO_MODAL;
import static com.tohant.om2d.storage.cache.GameCache.*;

@Component
@RequiredArgsConstructor
public class BuildRoomCommand implements Command {
    
    private final GameCache gameCache;
    private final CellDao cellDao;
    private final LevelDao levelDao;
    private final GameActorSearchService gameActorSearchService;
    private final CommonService commonService;
    private final AsyncRoomBuildService asyncRoomBuildService;

    @Override
    public void execute() {
        String cellId = gameCache.getValue(CURRENT_CELL);
        Cell cell = (Cell) gameActorSearchService.getActorById(cellId);
        CellEntity cellEntity = cellDao.queryForId(UUID.fromString(cellId));
        if (cellEntity.getRoomEntity() != null) {
            throw new GameException(GameException.Code.E000);
        }
        LevelEntity levelEntity = levelDao.queryForId(UUID.fromString(gameCache.getValue(CURRENT_LEVEL_ID)));
        String gridId = levelEntity.getId().toString();
        Grid grid = (Grid) gameActorSearchService.getActorById(gridId);
        DefaultModal roomInfoModal = (DefaultModal) gameActorSearchService.getActorById(ROOM_INFO_MODAL.name());
        Room nextRoom = null;
        Room.Type nextType = commonService.getCurrentRoomType();
        if (nextType != null) {
            float price = 0.0f;
            float cost = 0.0f;
            AtomicReference<Float> salaries = new AtomicReference<>(0.0f);
            if (checkNoCellOnGrid(grid.getChildren()) && nextType != Room.Type.HALL) {
                throw new GameException(GameException.Code.E200);
            } else if (nextType != Room.Type.HALL && commonService.nextToHalls(cell, gameActorSearchService) < 1) {
                throw new GameException(GameException.Code.E100);
            }
            float budget = gameCache.getFloat(CURRENT_BUDGET);
            if (budget >= price) {
                switch (nextType) {
                    case HALL: {
                        nextRoom = new HallRoom(null, new RoomInfo(cell.getName(), Array.with(), 100f, 20f, new TimeLineDate(12L, 1L, 1L), Room.Type.HALL),
                                cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case OFFICE: {
                        Array<Staff> workers = Array.with(IntStream.range(0, 7).boxed()
                                .map(i -> new WorkerStaff(null)).toArray(WorkerStaff[]::new));
                        List<String> workersIds = Arrays.stream(workers.toArray(Staff.class))
                                .map(Staff::getName).collect(Collectors.toList());
                        nextRoom = new OfficeRoom(null, new CompanyInfo(buildRandomCompanyName(), workersIds),
                                new RoomInfo(cell.getName(), workers, 550f, 50f, new TimeLineDate(15L, 1L, 1L), Room.Type.OFFICE),
                                cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case SECURITY: {
                        Array<Staff> security = Array.with(IntStream.range(0, 4).boxed()
                                .map(i -> {
                                    SecurityStaff s = new SecurityStaff(null, 1200.0f);
                                    salaries.updateAndGet(v -> v + s.getSalary());
                                    return s;
                                })
                                .toArray(SecurityStaff[]::new));
                        nextRoom = new SecurityRoom(null, new RoomInfo(cell.getName(), security, 910f, 100f, new TimeLineDate(25L, 1L, 1L), Room.Type.SECURITY),
                                cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case CLEANING: {
                        Array<Staff> cleaning = Array.with(IntStream.range(0, 2).boxed()
                                .map(i -> new CleaningStaff(null, 500.0f))
                                .map(s -> {
                                    salaries.updateAndGet(v -> v + s.getSalary());
                                    return s;
                                })
                                .toArray(CleaningStaff[]::new));
                        nextRoom = new CleaningRoom(null, new RoomInfo(cell.getName(), cleaning, 430f, 45f, new TimeLineDate(18L, 1L, 1L), Room.Type.CLEANING),
                                cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case CAFFE: {
                        Array<Staff> caffe = Array.with(IntStream.range(0, 1).boxed()
                                .map(i -> new CaffeStaff(null, Staff.Type.CAFFE.getSalary()))
                                .map(s -> {
                                    salaries.updateAndGet(v -> v + s.getSalary());
                                    return s;
                                })
                                .toArray(CaffeStaff[]::new));
                        nextRoom = new CaffeRoom(null, new RoomInfo(cell.getName(), caffe, Room.Type.CAFFE.getPrice(),
                                Room.Type.CAFFE.getCost(), new TimeLineDate(19L, 1L, 1L), Room.Type.CAFFE),
                                cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case ELEVATOR: {
                        nextRoom = new ElevatorRoom(null, new RoomInfo(cell.getName(), Array.with(), Room.Type.ELEVATOR.getPrice(),
                                Room.Type.ELEVATOR.getCost(), new TimeLineDate(14L, 1L, 1L), Room.Type.ELEVATOR),
                                cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                }
                gameCache.setFloat(CURRENT_BUDGET, budget - price);
                gameCache.setFloat(TOTAL_COSTS, gameCache.getFloat(TOTAL_COSTS) + cost);
                commonService.setRoomsAmountByType(nextRoom.getType(),
                        commonService.getRoomsAmountByType(nextRoom.getType()) + 1L);
                buildRoom(cell, nextRoom);
                ((ToggleActor) gameActorSearchService.getActorById(roomInfoModal.getName())).forceToggle(true);
                gameCache.setValue(CURRENT_CELL, cell.getName());
                AssetService.CONSTRUCTION_SOUND.play();
            }
        }
    }

    public void buildRoom(Cell cell, Room room) {
        ProgressBar buildStatus = new ProgressBar(0, room.getRoomInfo().getBuildTime().getDays(),
                1f, false, AssetsUtil.getDefaultSkin());
        buildStatus.setWidth(cell.getWidth() - DEFAULT_PAD / 2f);
        buildStatus.setPosition(buildStatus.getX() + DEFAULT_PAD / 4f,
                buildStatus.getY() + cell.getHeight() / 6f);
        cell.addActor(room);
        cell.addActor(buildStatus);
        cell.setEmpty(false);
        ((Array<RoomBuildingModel>) gameCache.getObject(BUILD_TASKS))
                .add(asyncRoomBuildService.submitBuild(cell, room));
    }

}

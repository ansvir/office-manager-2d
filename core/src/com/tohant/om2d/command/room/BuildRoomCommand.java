package com.tohant.om2d.command.room;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.Grid;
import com.tohant.om2d.actor.Office;
import com.tohant.om2d.actor.man.*;
import com.tohant.om2d.actor.room.*;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.command.ui.ForceToggleCommand;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.office.CompanyInfo;
import com.tohant.om2d.model.room.RoomInfo;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.model.task.TimeLineDate;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.AsyncRoomBuildService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;
import com.tohant.om2d.util.AssetsUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.actor.man.Staff.Type.*;
import static com.tohant.om2d.service.ServiceUtil.*;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.*;
import static com.tohant.om2d.storage.Cache.*;

public class BuildRoomCommand implements Command {

    private final AsyncRoomBuildService roomBuildService;
    private final int r, c;

    public BuildRoomCommand(int r, int c) {
        this.roomBuildService = AsyncRoomBuildService.getInstance();
        this.r = r;
        this.c = c;
    }

    @Override
    public void execute() {
        UiActorService uiActorService = UiActorService.getInstance();
        RuntimeCacheService cache = RuntimeCacheService.getInstance();
        String currentCompanyId = cache.getValue(CURRENT_COMPANY_ID);
        String currentOfficeId = cache.getValue(CURRENT_OFFICE_ID);
        String cellId = getCellActorId(r, c, (int) cache.getLong(CURRENT_LEVEL), currentOfficeId, currentCompanyId);
        String gridId = getGridActorId((int) cache.getLong(CURRENT_LEVEL), currentOfficeId, currentCompanyId);
        Cell cell = (Cell) uiActorService.getActorById(cellId);
        Grid grid = (Grid) uiActorService.getActorById(gridId);
        DefaultModal roomInfoModal = (DefaultModal) uiActorService.getActorById(ROOM_INFO_MODAL.name());
        Room nextRoom = null;
        Room.Type nextType = getCurrentRoomType();
        if (nextType != null) {
            float price = 0.0f;
            float cost = 0.0f;
            AtomicReference<Float> salaries = new AtomicReference<>(0.0f);
            if (checkNoCellOnGrid(grid.getChildren()) && nextType != Room.Type.HALL) {
                throw new GameException(GameException.Code.E200);
            } else if (nextType != Room.Type.HALL && nextToHalls(cell, grid.getChildren()) < 1) {
                throw new GameException(GameException.Code.E100);
            }
            float budget = cache.getFloat(CURRENT_BUDGET);
            if (budget >= price) {
                String roomId = ROOM.name() + ID_DELIMITER + cellId;
                String staffId = "STAFF" + COORD_DELIMITER + "%d" + ID_DELIMITER + roomId;
                switch (nextType) {
                    case HALL: {
                        nextRoom = new HallRoom(roomId, new RoomInfo(roomId, Array.with(), 100f, 20f, new TimeLineDate(12L, 1L, 1L), Room.Type.HALL),
                                cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case OFFICE: {
                        Array<Staff> workers = Array.with(IntStream.range(0, 7).boxed()
                                .map(i -> new WorkerStaff(staffId + i)).toArray(WorkerStaff[]::new));
                        List<String> workersIds = Arrays.stream(workers.toArray(Staff.class))
                                .map(Staff::getName).collect(Collectors.toList());
                        nextRoom = new OfficeRoom(roomId, new CompanyInfo(buildRandomCompanyName(), workersIds),
                                new RoomInfo(roomId, workers, 550f, 50f, new TimeLineDate(15L, 1L, 1L), Room.Type.OFFICE),
                                cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case SECURITY: {
                        Array<Staff> security = Array.with(IntStream.range(0, 4).boxed()
                                .map(i -> new SecurityStaff(String.format(staffId, i), 1200.0f))
                                .map(s -> {
                                    salaries.updateAndGet(v -> v + s.getSalary());
                                    return s;
                                })
                                .toArray(SecurityStaff[]::new));
                        nextRoom = new SecurityRoom(roomId, new RoomInfo(roomId, security, 910f, 100f, new TimeLineDate(25L, 1L, 1L), Room.Type.SECURITY),
                                cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case CLEANING: {
                        Array<Staff> cleaning = Array.with(IntStream.range(0, 2).boxed()
                                .map(i -> new CleaningStaff(String.format(staffId, i), 500.0f))
                                .map(s -> {
                                    salaries.updateAndGet(v -> v + s.getSalary());
                                    return s;
                                })
                                .toArray(CleaningStaff[]::new));
                        nextRoom = new CleaningRoom(roomId, new RoomInfo(roomId, cleaning, 430f, 45f, new TimeLineDate(18L, 1L, 1L), Room.Type.CLEANING),
                                cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case CAFFE: {
                        Array<Staff> caffe = Array.with(IntStream.range(0, 1).boxed()
                                .map(i -> new CaffeStaff(String.format(staffId, i), Staff.Type.CAFFE.getSalary()))
                                .map(s -> {
                                    salaries.updateAndGet(v -> v + s.getSalary());
                                    return s;
                                })
                                .toArray(CaffeStaff[]::new));
                        nextRoom = new CaffeRoom(roomId, new RoomInfo(roomId, caffe, Room.Type.CAFFE.getPrice(),
                                Room.Type.CAFFE.getCost(), new TimeLineDate(19L, 1L, 1L), Room.Type.CAFFE),
                                cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case ELEVATOR: {
                        nextRoom = new ElevatorRoom(roomId, new RoomInfo(roomId, Array.with(), Room.Type.ELEVATOR.getPrice(),
                                Room.Type.ELEVATOR.getCost(), new TimeLineDate(14L, 1L, 1L), Room.Type.ELEVATOR),
                                cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                }
                cache.setFloat(CURRENT_BUDGET, budget - price);
                cache.setFloat(TOTAL_COSTS, cache.getFloat(TOTAL_COSTS) + cost);
                setRoomsAmountByType(nextRoom.getType(), getRoomsAmountByType(nextRoom.getType()) + 1L);
                buildRoom(cell, nextRoom);
                new ForceToggleCommand(roomInfoModal.getName(), true).execute();
                cache.setValue(CURRENT_CELL, cellId);
                AssetService.getInstance().getConstructionSound().play();
            }
        }
    }

    public void buildRoom(Cell cell, Room room) {
        AsyncRoomBuildService roomBuildService = AsyncRoomBuildService.getInstance();
        ProgressBar buildStatus = new ProgressBar(0, room.getRoomInfo().getBuildTime().getDays(),
                1f, false, AssetsUtil.getDefaultSkin());
        buildStatus.setWidth(cell.getWidth() - DEFAULT_PAD / 2f);
        buildStatus.setPosition(buildStatus.getX() + DEFAULT_PAD / 4f,
                buildStatus.getY() + cell.getHeight() / 6f);
        cell.addActor(buildStatus);
        ((Array<RoomBuildingModel>) RuntimeCacheService.getInstance().getObject(BUILD_TASKS))
                .add(roomBuildService.submitBuild(cell, room));
    }

}

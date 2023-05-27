package com.tohant.om2d.command.room;

import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.Grid;
import com.tohant.om2d.actor.man.*;
import com.tohant.om2d.actor.room.*;
import com.tohant.om2d.actor.ui.modal.DefaultModal;
import com.tohant.om2d.command.AbstractCommand;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.room.RoomInfo;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.model.task.TimeLineDate;
import com.tohant.om2d.service.AsyncRoomBuildService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static com.tohant.om2d.service.ServiceUtil.*;
import static com.tohant.om2d.service.ServiceUtil.getRoomsAmountByType;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.*;
import static com.tohant.om2d.storage.Cache.*;

public class BuildRoomCommand extends AbstractCommand {

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
        String cellId = CELL.name() + "#" + r + "#" + c + "#" + cache.getLong(CURRENT_LEVEL);
        Cell cell = (Cell) uiActorService.getActorById(cellId);
        Grid grid = (Grid) uiActorService.getActorById(GRID.name() + "#" + cache.getLong(CURRENT_LEVEL));
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
                String roomId = ROOM.name() + "#" + r + "#" + c + "#" + cache.getLong(CURRENT_LEVEL);
                String staffId = STAFF.name() + "#" + r + "#" + c + "#";
                switch (nextType) {
                    case HALL: {
                        nextRoom = new HallRoom(roomId, new RoomInfo(Array.with(), 100f, 20f, new TimeLineDate(12L, 1L, 1L), Room.Type.HALL),
                                cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case OFFICE: {
                        Array<Staff> workers = Array.with(IntStream.range(0, 15).boxed()
                                .map(i -> new WorkerStaff(staffId + i)).toArray(WorkerStaff[]::new));
                        nextRoom = new OfficeRoom(roomId, new RoomInfo(workers, 550f, 50f, new TimeLineDate(15L, 1L, 1L), Room.Type.OFFICE),
                                cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case SECURITY: {
                        Array<Staff> security = Array.with(IntStream.range(0, 4).boxed()
                                .map(i -> new SecurityStaff(staffId + i, 1200.0f))
                                .map(s -> {
                                    salaries.updateAndGet(v -> v + s.getSalary());
                                    return s;
                                })
                                .toArray(SecurityStaff[]::new));
                        nextRoom = new SecurityRoom(roomId, new RoomInfo(security, 910f, 100f, new TimeLineDate(25L, 1L, 1L), Room.Type.SECURITY),
                                cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case CLEANING: {
                        Array<Staff> cleaning = Array.with(IntStream.range(0, 2).boxed()
                                .map(i -> new CleaningStaff(staffId + i, 500.0f))
                                .map(s -> {
                                    salaries.updateAndGet(v -> v + s.getSalary());
                                    return s;
                                })
                                .toArray(CleaningStaff[]::new));
                        nextRoom = new CleaningRoom(roomId, new RoomInfo(cleaning, 430f, 45f, new TimeLineDate(18L, 1L, 1L), Room.Type.CLEANING),
                                cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case CAFFE: {
                        Array<Staff> caffe = Array.with(IntStream.range(0, 1).boxed()
                                .map(i -> new CaffeStaff(staffId + i, Staff.Type.CAFFE.getSalary()))
                                .map(s -> {
                                    salaries.updateAndGet(v -> v + s.getSalary());
                                    return s;
                                })
                                .toArray(CaffeStaff[]::new));
                        nextRoom = new CaffeRoom(roomId, new RoomInfo(caffe, Room.Type.CAFFE.getPrice(),
                                Room.Type.CAFFE.getCost(), new TimeLineDate(19L, 1L, 1L), Room.Type.CAFFE),
                                cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                        price = nextRoom.getRoomInfo().getPrice();
                        cost += nextRoom.getRoomInfo().getCost();
                        break;
                    }
                    case ELEVATOR: {
                        nextRoom = new ElevatorRoom(roomId, new RoomInfo(Array.with(), Room.Type.ELEVATOR.getPrice(),
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
                cell.setRoomModel(new RoomBuildingModel(roomBuildService.submitBuild(nextRoom), nextRoom.getRoomInfo()));
                roomInfoModal.setVisible(true);
                cache.setValue(CURRENT_ROOM, cellId);
            }
        }
    }
}

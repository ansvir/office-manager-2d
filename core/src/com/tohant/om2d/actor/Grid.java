package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.tohant.om2d.actor.man.*;
import com.tohant.om2d.actor.room.*;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.exception.GameException.Code;
import com.tohant.om2d.model.room.RoomInfo;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.model.task.TimeLineDate;
import com.tohant.om2d.service.AsyncRoomBuildService;
import com.tohant.om2d.service.CacheService;
import com.tohant.om2d.stage.GameStage;
import com.tohant.om2d.storage.CacheProxy;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static com.tohant.om2d.service.ServiceUtil.nextToHalls;
import static com.tohant.om2d.service.ServiceUtil.checkNoCellOnGrid;
import static com.tohant.om2d.storage.CacheImpl.*;
import static com.tohant.om2d.storage.CacheImpl.CURRENT_BUDGET;
import static com.tohant.om2d.storage.CacheImpl.CURRENT_ROOM_TYPE;

public class Grid extends Group implements Disposable {

    private int cellSize;
    private Texture texture;
    private CacheProxy gameCache;
    private CacheService cacheService;
    private int cellsWidth, cellsHeight;
    private AsyncRoomBuildService roomBuildService;
    private boolean isGridVisible;

    public Grid(int x, int y, int cellsWidth, int cellsHeight, int cellSize) {
        this.cellsWidth = cellsWidth;
        this.cellsHeight = cellsHeight;
        setPosition(x, y);
        setSize(cellsWidth * cellSize, cellsHeight * cellSize);
        this.cellSize = cellSize;
        gameCache = new CacheProxy();
        cacheService = CacheService.getInstance();
        roomBuildService = AsyncRoomBuildService.getInstance();
        Pixmap pixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        Color borderColor = new Color(Color.GRAY);
        borderColor.a = 0.5f;
        pixmap.setColor(borderColor);
        pixmap.drawRectangle(1, 1, (int) getWidth() - 1, (int) getHeight() - 1);
        for (int i = 1; i <= cellsHeight; i++) {
            pixmap.drawLine(i * cellSize, 1, i * cellSize, cellSize * cellsHeight);
        }
        for (int i = 1; i <= cellsWidth; i++) {
            pixmap.drawLine(1, i * cellSize, cellSize * cellsWidth, i * cellSize);
        }
        for (int w = 0; w < cellsWidth; w++) {
            for (int h = 0; h < cellsHeight; h++) {
                Cell cell = new Cell(h * cellSize, w * cellSize, cellSize, cellSize);
                cell.setName("Cell#" + h + "#" + w);
                addCellEventHandling(cell);
                addActor(cell);
            }
        }
//        this.pixmap = pixmap;
        this.texture = new Texture(pixmap);
        pixmap.dispose();
        isGridVisible = true;
    }

    public float getCellSize() {
        return cellSize;
    }

    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (isGridVisible) {
            batch.draw(texture, getX(), getY(), cellsWidth * cellSize, cellsHeight * cellSize);
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    private void addCellEventHandling(Cell cell) {
        cell.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                Room nextRoom = null;
                Room.Type nextType = getCurrentRoomType();
                String currentId;
                if (cell.isEmpty() && nextType != null) {
                    float price = 0.0f;
                    float cost = 0.0f;
                    AtomicReference<Float> salaries = new AtomicReference<>(0.0f);
                    if (checkNoCellOnGrid(getChildren()) && nextType != Room.Type.HALL) {
                        ((GameStage) getStage()).addException(new GameException(Code.E200));
                        return false;
                    } else if (nextType != Room.Type.HALL && nextToHalls(cell, getChildren()) < 1) {
                        ((GameStage) getStage()).addException(new GameException(Code.E100));
                        return false;
                    }
                    float budget = Float.parseFloat((String) gameCache.getValue(CURRENT_BUDGET));
                    if (budget >= price) {
                        switch (nextType) {
                            case HALL: {
                                nextRoom = new HallRoom(new RoomInfo(Array.with(), 100f, 20f, new TimeLineDate(12L, 1L, 1L), Room.Type.HALL),
                                        cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                                price = nextRoom.getRoomInfo().getPrice();
                                cost += nextRoom.getRoomInfo().getCost();
                                break;
                            }
                            case OFFICE: {
                                Array<Staff> workers = Array.with(IntStream.range(0, 15).boxed()
                                        .map(i -> new WorkerStaff()).toArray(WorkerStaff[]::new));
                                nextRoom = new OfficeRoom(new RoomInfo(workers, 550f, 50f, new TimeLineDate(15L, 1L, 1L), Room.Type.OFFICE),
                                        cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                                price = nextRoom.getRoomInfo().getPrice();
                                cost += nextRoom.getRoomInfo().getCost();
                                break;
                            }
                            case SECURITY: {
                                Array<Staff> security = Array.with(IntStream.range(0, 4).boxed()
                                        .map(i -> new SecurityStaff(1200.0f))
                                        .map(s -> {
                                            salaries.updateAndGet(v -> v + s.getSalary());
                                            return s;
                                        })
                                        .toArray(SecurityStaff[]::new));
                                nextRoom = new SecurityRoom(new RoomInfo(security, 910f, 100f, new TimeLineDate(25L, 1L, 1L), Room.Type.SECURITY),
                                        cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                                price = nextRoom.getRoomInfo().getPrice();
                                cost += nextRoom.getRoomInfo().getCost();
                                break;
                            }
                            case CLEANING: {
                                Array<Staff> cleaning = Array.with(IntStream.range(0, 2).boxed()
                                        .map(i -> new CleaningStaff(500.0f))
                                        .map(s -> {
                                            salaries.updateAndGet(v -> v + s.getSalary());
                                            return s;
                                        })
                                        .toArray(CleaningStaff[]::new));
                                nextRoom = new CleaningRoom(new RoomInfo(cleaning, 430f, 45f, new TimeLineDate(18L, 1L, 1L), Room.Type.CLEANING),
                                        cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                                price = nextRoom.getRoomInfo().getPrice();
                                cost += nextRoom.getRoomInfo().getCost();
                                break;
                            }
                            case CAFFE: {
                                Array<Staff> caffe = Array.with(IntStream.range(0, 1).boxed()
                                        .map(i -> new CaffeStaff(Staff.Type.CAFFE.getSalary()))
                                        .map(s -> {
                                            salaries.updateAndGet(v -> v + s.getSalary());
                                            return s;
                                        })
                                        .toArray(CaffeStaff[]::new));
                                nextRoom = new CaffeRoom(new RoomInfo(caffe, Room.Type.CAFFE.getPrice(),
                                        Room.Type.CAFFE.getCost(), new TimeLineDate(19L, 1L, 1L), Room.Type.CAFFE),
                                        cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                                price = nextRoom.getRoomInfo().getPrice();
                                cost += nextRoom.getRoomInfo().getCost();
                                break;
                            }
                        }
                        cacheService.setFloat(CURRENT_BUDGET, budget - price);
                        cacheService.setFloat(TOTAL_COSTS, cacheService.getFloat(TOTAL_COSTS) + cost);
                        setRoomsAmountByType(nextRoom.getType(), getRoomsAmountByType(nextRoom.getType()) + 1L);
                        cell.setRoomModel(new RoomBuildingModel(roomBuildService.submitBuild(nextRoom), nextRoom.getRoomInfo()));
                        currentId = nextRoom.getRoomInfo().getId();
                        ((GameStage) getStage()).getRoomInfoModal().getThis().setVisible(true);
                        cacheService.setValue(CURRENT_ROOM, currentId);
                        return false;
                    }
                } else if (!cell.isEmpty()) {
                    currentId = cell.getRoomModel().getRoomInfo().getId();
                    ((GameStage) getStage()).getRoomInfoModal().getThis().setVisible(true);
                    cacheService.setValue(CURRENT_ROOM, currentId);
                    return false;
                } else {
                    ((GameStage) getStage()).getRoomInfoModal().getThis().setVisible(false);
                    cacheService.setValue(CURRENT_ROOM, null);
                    return false;
                }
                return true;
            }
        });
    }

    public Room.Type getCurrentRoomType() {
        String value = (String) gameCache.getValue(CURRENT_ROOM_TYPE);
        if (value == null) {
            return null;
        } else {
            return Room.Type.valueOf(value);
        }
    }

    private long getRoomsAmountByType(Room.Type type) {
        switch (type) {
            case OFFICE: return cacheService.getLong(OFFICES_AMOUNT);
            case HALL: return cacheService.getLong(HALLS_AMOUNT);
            case SECURITY: return cacheService.getLong(SECURITY_AMOUNT);
            case CLEANING: return cacheService.getLong(CLEANING_AMOUNT);
            case CAFFE: return cacheService.getLong(CAFFE_AMOUNT);
            default: return -1L;
        }
    }

    private void setRoomsAmountByType(Room.Type type, long amount) {
        switch (type) {
            case OFFICE: cacheService.setLong(OFFICES_AMOUNT, amount); break;
            case HALL: cacheService.setLong(HALLS_AMOUNT, amount); break;
            case SECURITY: cacheService.setLong(SECURITY_AMOUNT, amount); break;
            case CLEANING: cacheService.setLong(CLEANING_AMOUNT, amount); break;
            case CAFFE: cacheService.setLong(CAFFE_AMOUNT, amount); break;
            default: break;
        }
    }

    public void setIsGridVisible(boolean isVisible) {
        this.isGridVisible = isVisible;
    }

    public boolean isGridVisible() {
        return isGridVisible;
    }

}
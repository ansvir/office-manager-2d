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
import com.tohant.om2d.actor.man.CleaningStaff;
import com.tohant.om2d.actor.man.SecurityStaff;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.actor.man.WorkerStaff;
import com.tohant.om2d.actor.room.*;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.exception.GameException.Code;
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
    private int cellsWidth, cellsHeight;

    public Grid(int x, int y, int cellsWidth, int cellsHeight, int cellSize) {
        this.cellsWidth = cellsWidth;
        this.cellsHeight = cellsHeight;
        setPosition(x, y);
        setSize(cellsWidth * cellSize, cellsHeight * cellSize);
        this.cellSize = cellSize;
        gameCache = new CacheProxy();
        Pixmap pixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        Color borderColor = Color.GRAY;
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
//                pixmap.drawLine(i, j, i, j + cellSize);
//                pixmap.drawLine(i, j, i + cellSize, j);
                Cell cell = new Cell(h * cellSize, w * cellSize, cellSize, cellSize);
                cell.setName("Cell#" + h + "#" + w);
                addCellEventHandling(cell);
                addActor(cell);
            }
        }
        texture = new Texture(pixmap);
        pixmap.dispose();
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
        batch.draw(texture, getX(), getY(), cellsWidth * cellSize, cellsHeight * cellSize);
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
                if (cell.isEmpty()) {
                    float price = 0.0f;
                    float cost = Float.parseFloat((String) gameCache.getValue(TOTAL_COSTS));
                    AtomicReference<Float> salaries = new AtomicReference<>(0.0f);
                    Room newRoom = null;
                    Room.Type nextType = getCurrentRoomType();
                    if (nextType == null) {
                        return false;
                    }
                    if (checkNoCellOnGrid(getChildren()) && nextType != Room.Type.HALL) {
                        ((GameStage) getStage()).addException(new GameException(Code.E200));
                        return false;
                    } else if (nextType != Room.Type.HALL && nextToHalls(cell, getChildren()) < 1) {
                        ((GameStage) getStage()).addException(new GameException(Code.E100));
                        return false;
                    }
                    switch (nextType) {
                        case HALL: {
                            newRoom = new HallRoom(100f, 20f, cell.getX(), cell.getY(),
                                    cell.getWidth(), cell.getHeight());
                            price = newRoom.getPrice();
                            cost += newRoom.getCost();
                            break;
                        }
                        case OFFICE: {
                            Array<Staff> workers = Array.with(IntStream.range(0, 15).boxed()
                                    .map(i -> new WorkerStaff()).toArray(WorkerStaff[]::new));
                            newRoom = new OfficeRoom(workers, 550f, 50f, cell.getX(), cell.getY(),
                                    cell.getWidth(), cell.getHeight());
                            price = newRoom.getPrice();
                            cost += newRoom.getCost();
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
                            newRoom = new SecurityRoom(security, 910f, 100f, cell.getX(), cell.getY(),
                                    cell.getWidth(), cell.getHeight());
                            price = newRoom.getPrice();
                            cost += newRoom.getCost();
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
                            newRoom = new CleaningRoom(cleaning, 430f, 45f, cell.getX(), cell.getY(),
                                    cell.getWidth(), cell.getHeight());
                            price = newRoom.getPrice();
                            cost += newRoom.getCost();
                            break;
                        }
                    }
                    float budget = Float.parseFloat((String) gameCache.getValue(CURRENT_BUDGET));
                    if (budget >= price) {
                        gameCache.setValue(CURRENT_BUDGET, budget - price);
                        gameCache.setValue(TOTAL_SALARIES, Float.parseFloat((String) gameCache.getValue(TOTAL_SALARIES)) + salaries.get());
                        gameCache.setValue(TOTAL_COSTS, Float.parseFloat((String) gameCache.getValue(TOTAL_COSTS)) + cost);
                        if (newRoom instanceof OfficeRoom) {
                            gameCache.setValue(TOTAL_INCOMES, Float.parseFloat((String) gameCache.getValue(TOTAL_INCOMES)) + 100.0f * newRoom.getStaff().size);
                        }
                        setRoomsAmountByType(newRoom.getType(), getRoomsAmountByType(newRoom.getType()) + 1L);
                        cell.setRoom(newRoom);
                    }
                }
                gameCache.setValue(CURRENT_ROOM, cell.getRoom().getId());
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
            case OFFICE: return Long.parseLong((String) gameCache.getValue(OFFICES_AMOUNT));
            case HALL: return Long.parseLong((String) gameCache.getValue(HALLS_AMOUNT));
            case SECURITY: return Long.parseLong((String) gameCache.getValue(SECURITY_AMOUNT));
            case CLEANING: return Long.parseLong((String) gameCache.getValue(CLEANING_AMOUNT));
            default: return -1L;
        }
    }

    private void setRoomsAmountByType(Room.Type type, long amount) {
        switch (type) {
            case OFFICE: gameCache.setValue(OFFICES_AMOUNT, amount); break;
            case HALL: gameCache.setValue(HALLS_AMOUNT, amount); break;
            case SECURITY: gameCache.setValue(SECURITY_AMOUNT, amount); break;
            case CLEANING: gameCache.setValue(CLEANING_AMOUNT, amount); break;
            default: break;
        }
    }

}
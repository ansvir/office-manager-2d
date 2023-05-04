package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Disposable;
import com.tohant.om2d.storage.Cache;
import com.tohant.om2d.storage.CacheImpl;
import com.tohant.om2d.storage.CacheProxy;

import static com.tohant.om2d.storage.CacheImpl.*;
import static com.tohant.om2d.storage.CacheImpl.CURRENT_BUDGET;
import static com.tohant.om2d.storage.CacheImpl.CURRENT_ROOM_TYPE;

public class Grid extends Group implements Disposable {

    private float cellSize;
    private Texture texture;
    private CacheProxy gameCache;

    public Grid(int x, int y, float width, float height, int cellSize) {
        float cellsWidth = width / cellSize;
        float cellsHeight = height / cellSize;
        setPosition(x, y);
        setSize(width, height);
        this.cellSize = cellSize;
        gameCache = new CacheProxy((c) -> {}, (c) -> {}, (c) -> {
            c.setValue(CURRENT_ROOM_TYPE, null);
            c.setValue(CURRENT_BUDGET, 2000.0f);
            c.setValue(CURRENT_TIME, "01/01/0001");
            c.setValue(OFFICES_AMOUNT, 0L);
            c.setValue(HALLS_AMOUNT, 0L);
            c.setValue(SECURITY_AMOUNT, 0L);
            c.setValue(CLEANING_AMOUNT, 0L);
            c.setValue(IS_PAYDAY, false);
            c.setValue(CURRENT_ROOM, null);
            c.setValue(TOTAL_COSTS, 0.0f);
        });
        Pixmap pixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        Color borderColor = Color.GRAY;
        borderColor.a = 0.5f;
        pixmap.setColor(borderColor);
        for (int i = 0, w = 0; i < getWidth() && w < cellsWidth; i += cellSize, w++) {
            for (int j = 0, h = 0; j < getHeight() && h < cellsHeight; j += cellSize, h++) {
                pixmap.drawLine(i, j, i, j + cellSize);
                pixmap.drawLine(i, j, i + cellSize, j);
                Cell cell = new Cell(i, getHeight() - j, cellSize, cellSize);
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

    public void setCellSize(float cellSize) {
        this.cellSize = cellSize;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
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
                    Room newRoom = null;
                    Room.Type nextType = getCurrentRoomType();
                    if (nextType == null) {
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
                            newRoom = new OfficeRoom(550f, 50f, cell.getX(), cell.getY(),
                                    cell.getWidth(), cell.getHeight());
                            price = newRoom.getPrice();
                            cost += newRoom.getCost();
                            break;
                        }
                        case SECURITY: {
                            newRoom = new SecurityRoom(910f, 100f, cell.getX(), cell.getY(),
                                    cell.getWidth(), cell.getHeight());
                            price = newRoom.getPrice();
                            cost += newRoom.getCost();
                            break;
                        }
                        case CLEANING: {
                            newRoom = new CleaningRoom(430f, 45f, cell.getX(), cell.getY(),
                                    cell.getWidth(), cell.getHeight());
                            price = newRoom.getPrice();
                            cost += newRoom.getCost();
                            break;
                        }
                    }
                    float budget = Float.parseFloat((String) gameCache.getValue(CURRENT_BUDGET));
                    if (budget >= price) {
                        gameCache.setValue(CURRENT_BUDGET, budget - price);
                        gameCache.setValue(TOTAL_COSTS, cost);
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
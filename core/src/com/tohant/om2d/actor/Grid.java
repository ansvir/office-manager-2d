package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.tohant.om2d.storage.GameCache;

public class Grid extends Group implements Disposable {

    private float cellSize;
    private Texture texture;
    private GameCache gameCache;

    public Grid(int x, int y, float width, float height, int cellSize) {
        float cellsWidth = width / cellSize;
        float cellsHeight = height / cellSize;
        setPosition(x, y);
        setSize(width, height);
        gameCache = new GameCache();
        this.cellSize = cellSize;
        Pixmap pixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        Color borderColor = Color.GRAY;
        borderColor.a = 0.5f;
        pixmap.setColor(borderColor);
        for (int i = 0, w = 0; i < getWidth() && w < cellsWidth; i += cellSize, w++) {
            for (int j = 0, h = 0; j < getHeight() && h < cellsHeight; j += cellSize, h++) {
                pixmap.drawLine(i, j, i, j + cellSize);
                pixmap.drawLine(i, j, i + cellSize, j);
                Cell cell = new Cell(i, getHeight() - j, cellSize, cellSize);
                cell.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchDown(event, x, y, pointer, button);
                        float cost = 0.0f;
                        Room newRoom = null;
                        Room.Type nextType = gameCache.getRoomType();
                        if (nextType == null) {
                            return false;
                        }
                        switch (nextType) {
                            case HALL: {
                                newRoom = new HallRoom(100f, 20f, cell.getX(), cell.getY(),
                                        cell.getWidth(), cell.getHeight());
                                cost += 100f;
                                break;
                            }
                            case OFFICE: {
                                newRoom = new OfficeRoom(550f, 50f, cell.getX(), cell.getY(),
                                        cell.getWidth(), cell.getHeight());

                                cost += 550f;
                                break;
                            }
                            case SECURITY: {
                                newRoom = new SecurityRoom(910f, 100f, cell.getX(), cell.getY(),
                                        cell.getWidth(), cell.getHeight());
                                cost += 910f;
                                break;
                            }
                        }
                        float budget = gameCache.getBudget();
                        if (budget >= cost) {
                            gameCache.setBudget(budget - cost);
                            gameCache.setRoomsAmountByType(newRoom.getType(), gameCache.getRoomsAmountByType(newRoom.getType()) + 1L);
                            cell.setRoom(newRoom);
                        }
                        return true;
                    }
                });
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

}
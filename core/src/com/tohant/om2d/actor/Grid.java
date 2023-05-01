package com.tohant.om2d.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.tohant.om2d.storage.GameCache;

public class Grid extends Group implements Disposable {

    private float cellSize;
    private Texture texture;
    private GameCache gameCache;
    private float budget;

    public Grid(int x, int y, float width, float height, int cellSize) {
        setPosition(x, y);
        setSize(width + 1, height + 1);
        gameCache = new GameCache();
        budget = gameCache.getBudget();
        this.cellSize = cellSize;
        Pixmap pixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        Color borderColor = Color.GRAY;
        borderColor.a = 0.5f;
        pixmap.setColor(borderColor);
        for (int i = 0; i < getWidth(); i += cellSize) {
            for (int j = 0; j < getHeight(); j += cellSize) {
                pixmap.drawLine(i, j, i, j + cellSize);
                pixmap.drawLine(i, j, i + cellSize, j);
                Cell cell = new Cell(i, getHeight() - j, cellSize, cellSize);
                cell.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        float cost = 0.0f;
                        Room newRoom = null;
                        switch (gameCache.getRoomType()) {
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
                        if (budget >= cost) {
                            gameCache.setBudget(budget - cost);
                            gameCache.setRoomsAmountByType(newRoom.getType(), gameCache.getRoomsAmountByType(newRoom.getType()) + 1L);
                            cell.setRoom(newRoom);
                            budget -= cost;
                        }
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
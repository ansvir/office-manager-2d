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
                        switch (gameCache.getRoomType()) {
                            case HALL: {
                                cell.setRoom(new HallRoom(100f, cell.getX(), cell.getY(),
                                        cell.getWidth(), cell.getHeight()));
                                gameCache.setBudget(budget - 100f);
                                budget -= 100f;
                                break;
                            }
                            case OFFICE: {
                                cell.setRoom(new OfficeRoom(550f, cell.getX(), cell.getY(),
                                        cell.getWidth(), cell.getHeight()));
                                gameCache.setBudget(budget - 550f);
                                budget -= 550f;
                                break;
                            }
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
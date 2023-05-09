package com.tohant.om2d.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import static com.tohant.om2d.actor.constant.Constant.*;

public class Map extends Group {

    private Grid grid;
    private Background background;
    private float startX, startY;

    public Map(float x, float y, float width, float height) {
        setPosition(x, y);
        setSize(width, height);
        this.background = new Background((int) x, (int) y, (int) width, (int) height);
        this.grid = new Grid(Math.round(background.getWidth() / 2f - (GRID_WIDTH * CELL_SIZE) / 2f),
                Math.round(background.getHeight() / 2f - (GRID_HEIGHT * CELL_SIZE) / 2f),
                GRID_WIDTH, GRID_HEIGHT, CELL_SIZE);
        addActor(background);
        addActor(grid);
        addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                super.mouseMoved(event, x, y);
                grid.getChildren().forEach(c -> {
                    if (c instanceof Cell) {
                        Cell current = (Cell) c;
                        Rectangle cell = new Rectangle(current.getX(), current.getY(), current.getWidth(), current.getHeight());
                        Vector3 newCoords = event.getStage().getCamera().project(new Vector3(x, y, 0));
                        Vector2 mouse = new Vector2(newCoords.x - grid.getX(), newCoords.y - grid.getY());
                        current.setActive(cell.contains(mouse));
                    }
                });
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                return false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                startX = x;
                startY = y;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.AllResize);
//                background.setPosition(MathUtils.clamp(background.getX() + (startX - x), 0, background.getWidth()),
//                        MathUtils.clamp(background.getY() - (startY - y),
//                                0, background.getHeight()));
//                grid.setPosition(grid.getX() + (startX - x), grid.getY() - (startY - y));
//                grid.setPosition(MathUtils.clamp(grid.getX() + (startX - x),
//                                0, background.getWidth() - grid.getWidth()),
//                        MathUtils.clamp(grid.getY() - (startY - y),
//                                0, background.getHeight() - grid.getHeight()));
//                grid.setPosition(Math.round((background.getX() + background.getWidth()) / 2f - (grid.getX() + grid.getWidth()) / 2f),
//                        Math.round((background.getY() + background.getHeight()) / 2f - (grid.getY() + grid.getHeight()) / 2f));
                System.out.println(getX() + " " + getY());
                System.out.println(getX() + (startX - x));
                setPosition(MathUtils.clamp(MathUtils.lerp(getX(),  startX - x, 0.2f), - getWidth() + grid.getWidth(), 0),
                        MathUtils.clamp(MathUtils.lerp(getY(), startY - y, 0.2f), - getHeight() + grid.getHeight(), 0));
//                setPosition(MathUtils.clamp(getX() + (startX - x), 0, getWidth() - (Gdx.graphics.getWidth() - getWidth())),
//                        MathUtils.clamp(getY() - (startY - y),
//                                0, getHeight() - (Gdx.graphics.getHeight() - getHeight())));
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
//        background.setPosition(grid.getX() - (getWidth() - grid.getWidth() - grid.getX()),
//                grid.getY() - (getHeight() - grid.getHeight() - grid.getY()));
    }

    public Grid getGrid() {
        return grid;
    }

}

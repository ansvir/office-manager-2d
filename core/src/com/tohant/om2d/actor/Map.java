package com.tohant.om2d.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;

public class Map extends Group {

    private Grid grid;
    private float startX, startY;

    public Map(float x, float y, float width, float height, Grid grid) {
        setPosition(x, y);
        setSize(width, height);
        this.grid = grid;
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
                grid.setPosition(MathUtils.clamp(grid.getX() + (startX - x),
                                0, Gdx.graphics.getWidth() - grid.getWidth()),
                        MathUtils.clamp(grid.getY() - (startY - y),
                                0, Gdx.graphics.getHeight() - grid.getHeight()));
            }
        });
    }

}

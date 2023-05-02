package com.tohant.om2d.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.Grid;

public class GameMainStage extends Stage implements InputProcessor {

    private static final float GRID_SIZE = 1000;

    private float startX, startY;
    private Grid grid;
    public GameMainStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        grid = new Grid((int) ((Gdx.graphics.getWidth() / 2f) - (GRID_SIZE / 2)),
                ((int) ((Gdx.graphics.getHeight() / 2f) - (GRID_SIZE / 2))),
                GRID_SIZE, GRID_SIZE, (int) GRID_SIZE / 15);
        addActor(grid);
    }

    public GameMainStage(Viewport viewport, Batch batch, Grid grid) {
        super(viewport, batch);
        this.grid = grid;
        addActor(grid);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        grid.getChildren().forEach(c -> {
            if (c instanceof Cell) {
                Cell current = (Cell) c;
                Rectangle cell = new Rectangle(current.getX(), current.getY(), current.getWidth(), current.getHeight());
                Vector3 newCoords = this.getCamera().unproject(new Vector3(screenX, screenY, 0));
                Vector2 mouse = new Vector2(newCoords.x - grid.getX(), newCoords.y - grid.getY());
                current.setActive(cell.contains(mouse));
            }
        });
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);
        startX = screenX;
        startY = screenY;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        super.touchDragged(screenX, screenY, pointer);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.AllResize);
        grid.setPosition(MathUtils.clamp(grid.getX() + (startX - screenX),
                        0, Gdx.graphics.getWidth() - grid.getWidth()),
                MathUtils.clamp(grid.getY() - (startY - screenY),
                        0, Gdx.graphics.getHeight() - grid.getHeight()));
        return false;
    }

}

package com.tohant.om2d.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tohant.om2d.actor.Cell;
import com.tohant.om2d.actor.Grid;

public class GameMainStage extends Stage implements InputProcessor {

    private static final float GRID_SIZE = 1000;

    private Grid grid;

    public GameMainStage(Viewport viewport, Batch batch) {
        super(viewport, batch);
        grid = new Grid((int) ((Gdx.graphics.getWidth() / 2f) - (GRID_SIZE / 2)),
                ((int) ((Gdx.graphics.getHeight() / 2f) - (GRID_SIZE / 2))),
                GRID_SIZE, GRID_SIZE, (int) GRID_SIZE / 20);
        addActor(grid);
    }

    public GameMainStage(Viewport viewport, Batch batch, Grid grid) {
        super(viewport, batch);
        this.grid = grid;
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
        return true;
    }

}

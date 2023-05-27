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
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.GRID;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.OFFICE;
import static com.tohant.om2d.storage.Cache.*;

public class Map extends Group {

    private float startX, startY;
    private final AssetService assetService;
    private final RuntimeCacheService runtimeCacheService;

    public Map(String id) {
        setName(id);
        this.runtimeCacheService = RuntimeCacheService.getInstance();
        this.runtimeCacheService.setLong(CURRENT_OFFICE_CELLS_WIDTH, GRID_WIDTH);
        this.runtimeCacheService.setLong(CURRENT_OFFICE_CELLS_HEIGHT, GRID_HEIGHT);
//        this.background = new Background((int) x, (int) y, (int) width, (int) height);
//        this.office = new Office(Math.round(background.getWidth() / 2f - (GRID_WIDTH * CELL_SIZE) / 2f),
//                Math.round(background.getHeight() / 2f - (GRID_HEIGHT * CELL_SIZE) / 2f), GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE);
//        Grid grid = new Grid(0, 0, GRID_WIDTH, GRID_HEIGHT, CELL_SIZE, 0);
//        this.office.addActor(grid);
//        this.office.setCurrentLevelNumber(0);
        this.assetService = AssetService.getInstance();
//        setPosition(Math.round(x - office.getX() + (Gdx.graphics.getWidth() / 2f) - (office.getWidth() / 2f)),
//                Math.round(y - office.getY() + (Gdx.graphics.getHeight() / 2f) - (office.getHeight() / 2f)));
        addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                super.mouseMoved(event, x, y);
                UiActorService uiActorService = UiActorService.getInstance();
                Office office = (Office) uiActorService.getActorById(OFFICE.name());
                Grid grid = (Grid) uiActorService.getActorById(GRID.name() + "#" + runtimeCacheService.getLong(CURRENT_LEVEL));
                grid.getChildren().forEach(c -> {
                    if (c instanceof Cell) {
                        Cell current = (Cell) c;
                        Rectangle cell = new Rectangle(current.getX(), current.getY(), current.getWidth(), current.getHeight());
                        Vector3 newCoords = event.getStage().getCamera().project(new Vector3(x, y, 0));
                        Vector2 mouse = new Vector2(newCoords.x - office.getX(), newCoords.y - office.getY());
                        current.setActive(cell.contains(mouse));
                    }
                });
                Gdx.graphics.setCursor(assetService.getDefaultCursor());
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
                UiActorService uiActorService = UiActorService.getInstance();
                Office office = (Office) uiActorService.getActorById(OFFICE.name());
                Gdx.graphics.setSystemCursor(Cursor.SystemCursor.AllResize);
                setPosition(Math.round(MathUtils.clamp(MathUtils.lerp(getX(),  startX - x, 0.05f), - getWidth() + office.getWidth(), 0)),
                        Math.round(MathUtils.clamp(MathUtils.lerp(getY(), startY - y, 0.05f), - getHeight() + office.getHeight(), 0)));
            }
        });
    }

}

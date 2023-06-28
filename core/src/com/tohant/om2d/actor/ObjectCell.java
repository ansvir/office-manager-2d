package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.command.item.PlaceItemCommand;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.RuntimeCacheService;

import static com.tohant.om2d.storage.Cache.*;

public class ObjectCell extends Group {

    private boolean isObstacle;
    private boolean isActive;
    private boolean isGridVisible;

    public ObjectCell(String id, float x, float y, float width, float height, boolean isObstacle) {
        setName(id);
        setPosition(x, y);
        setSize(width, height);
        this.isObstacle = isObstacle;
        addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (RuntimeCacheService.getInstance().getObject(CURRENT_ITEM) != null) {
                    RuntimeCacheService runtimeCache = RuntimeCacheService.getInstance();
                    runtimeCache.setObject(CURRENT_OBJECT_CELL, ObjectCell.this);
                    setActive(true);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                setActive(false);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                if (RuntimeCacheService.getInstance().getObject(CURRENT_ITEM) != null) {
                    try {
                        new PlaceItemCommand().execute();
                    } catch (GameException e) {
                        RuntimeCacheService cacheService = RuntimeCacheService.getInstance();
                        Array<GameException> exceptions = (Array<GameException>) cacheService.getObject(GAME_EXCEPTION);
                        exceptions.add(e);
                    }
                }
                return false;
            }
        });
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    public void setObstacle(boolean obstacle) {
        isObstacle = obstacle;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isGridVisible() {
        return isGridVisible;
    }

    public void setGridVisible(boolean gridVisible) {
        isGridVisible = gridVisible;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (isGridVisible) {
            AssetService assetService = AssetService.getInstance();
            if (isActive) {
                batch.draw(assetService.getActiveObjectCellTexture(), getX(), getY());
            } else {
                batch.draw(assetService.getObjectCellBordersTexture(), getX(), getY());
            }
        }
    }

}

package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.storage.cache.Cache;
import com.tohant.om2d.command.Command;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.service.AssetService;

import static com.tohant.om2d.service.ServiceUtil.*;

public class Cell extends Group implements ToggleActor {

    private boolean isEmpty;
    private boolean isActive;
    private boolean isGridVisible;

    public Cell(String id, Command command, float x, float y, float width, float height, Room room, String items) {
        initCell(id, command, x, y, width, height);
        if (room != null) {
            addActor(room);
        }
        addObjectCellsAndStaff(this, room, items);
    }

    private void initCell(String id, Command command, float x, float y, float width, float height) {
        setName(id);
        setPosition(x, y);
        setSize(width, height);
        isEmpty = true;
        addListener(new InputListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                setActive(true);
                if (!isEmpty && hasChildren()) {
                    getChildren().iterator().forEach(c -> {
                        if (c instanceof ObjectCell) {
                            ((ObjectCell) c).setGridVisible(false);
                        }
                    });
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                setActive(false);
                if (isEmpty && hasChildren()) {
                    getChildren().iterator().forEach(c -> {
                        if (c instanceof ObjectCell) {
                            ((ObjectCell) c).setGridVisible(false);
                        }
                    });
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                try {
                    command.execute();
                } catch (GameException e) {
                    RuntimeCacheService cacheService = RuntimeCacheService.getInstance();
                    Array<GameException> exceptions = (Array<GameException>) cacheService.getObject(Cache.GAME_EXCEPTION);
                    exceptions.add(e);
                }
                return false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isActive && isEmpty) {
            batch.draw(AssetService.getInstance().getActiveEmptyCellTexture(), getX(), getY());
        }
        super.draw(batch, parentAlpha);
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isBuilt() {
        Room room = getCellRoom(this);
        if (room == null) {
            return false;
        }
        RuntimeCacheService cacheService = RuntimeCacheService.getInstance();
        Array<RoomBuildingModel> tasks = (Array<RoomBuildingModel>) cacheService.getObject(Cache.BUILD_TASKS);
        for (RoomBuildingModel m : tasks) {
            if (m.getRoomInfo().getId().equals(room.getRoomInfo().getId()) && !m.getRoom().isDone()) {
                return false;
            }
        }
        return true;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public void toggle() {
        isGridVisible = !isGridVisible;
    }

    @Override
    public void forceToggle(boolean value) {
        isGridVisible = value;
    }

}

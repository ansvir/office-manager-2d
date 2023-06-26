package com.tohant.om2d.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.common.storage.Command;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.AsyncRoomBuildService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

import java.util.concurrent.ExecutionException;

import static com.tohant.om2d.actor.constant.Constant.*;
import static com.tohant.om2d.service.ServiceUtil.getOfficeActorId;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.OBJECT_CELL;
import static com.tohant.om2d.storage.Cache.*;
import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

public class Cell extends Group implements ToggleActor {

    private Array<Array<ObjectCell>> objectCells;
    private boolean isEmpty;
    private boolean isActive;
    private final Skin skin;
    private boolean isGridVisible;

    public Cell(String id, Command command, float x, float y, float width, float height) {
        setName(id);
        setPosition(x, y);
        setSize(width, height);
        isEmpty = true;
        this.skin = getDefaultSkin();
        addListener(new InputListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                setActive(true);
                if (!isEmpty) {
                    objectCells.iterator().forEach(c -> c.iterator().forEach(c1 -> c1.setGridVisible(true)));
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                setActive(false);
                objectCells.iterator().forEach(c -> c.iterator().forEach(c1 -> c1.setGridVisible(false)));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                try {
                    command.execute();
                } catch (GameException e) {
                    RuntimeCacheService cacheService = RuntimeCacheService.getInstance();
                    Array<GameException> exceptions = (Array<GameException>) cacheService.getObject(GAME_EXCEPTION);
                    exceptions.add(e);
                    cacheService.setObject(GAME_EXCEPTION, exceptions);
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

    public void destroyRoom() {
        clearChildren();
        UiActorService uiActorService = UiActorService.getInstance();
        RuntimeCacheService cache = RuntimeCacheService.getInstance();
        String currentCompanyId = cache.getValue(CURRENT_COMPANY_ID);
        String currentOfficeId = cache.getValue(CURRENT_OFFICE_ID);
        String officeId = getOfficeActorId(currentOfficeId, currentCompanyId);
        Office office = (Office) uiActorService.getActorById(officeId);
        .getRoomInfo().getStaff().forEach(office::removeActor);
        isEmpty = true;
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

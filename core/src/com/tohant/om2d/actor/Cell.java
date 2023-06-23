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
import com.tohant.om2d.command.AbstractCommand;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.AsyncRoomBuildService;
import com.tohant.om2d.service.RuntimeCacheService;
import com.tohant.om2d.service.UiActorService;

import java.util.concurrent.ExecutionException;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.actor.constant.Constant.OBJECT_CELL_SIZE;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.OBJECT_CELL;
import static com.tohant.om2d.service.UiActorService.UiComponentConstant.OFFICE;
import static com.tohant.om2d.storage.Cache.GAME_EXCEPTION;
import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

public class Cell extends Group implements ToggleActor {

    private final AbstractCommand command;

    private Array<Array<ObjectCell>> objectCells;
    private RoomBuildingModel roomModel;
    private boolean isEmpty;
    private boolean isActive;
    private final AssetService assetService;
    private ProgressBar buildStatus;
    private final Skin skin;
    private final AsyncRoomBuildService roomBuildService;
    private TimeLineTask<Room> buildTask;
    private final RuntimeCacheService cacheService;
    private boolean isGridVisible;

    public Cell(String id, AbstractCommand command, float x, float y, float width, float height) {
        setName(id);
        this.command = command;
        setPosition(x, y);
        setSize(width, height);
        isEmpty = true;
        this.assetService = AssetService.getInstance();
        this.skin = getDefaultSkin();
        this.roomBuildService = AsyncRoomBuildService.getInstance();
        cacheService = RuntimeCacheService.getInstance();
        addListener(new InputListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (!isGridVisible) {
                    setActive(true);
                } else if (roomModel != null && roomModel.getRoom().isDone()) {
                    objectCells.iterator().forEach(c -> c.iterator().forEach(c1 -> c1.setGridVisible(true)));
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                setActive(false);
                if (isGridVisible) {
                    if (roomModel != null && roomModel.getRoom().isDone()) {
                        objectCells.iterator().forEach(c -> c.iterator().forEach(c1 -> c1.setGridVisible(false)));
                    }
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                try {
                    command.execute();
                } catch (GameException e) {
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
        if (!isEmpty) {
            if (roomModel.getRoom().isDone()) {
                try {
                    roomModel.getRoom().get().draw(batch, parentAlpha);
                    removeActor(this.buildStatus);
                } catch (InterruptedException | ExecutionException ignored) {
                    if (roomModel.getRoom().isCancelled()) {
                        setRoomModel(null);
                    }
                }
            } else {
                this.buildStatus.setValue(this.buildTask.getDate().getDays());
                batch.draw(assetService.getRoomConstructionTexture(),getX(), getY());
            }
        }
        if (isActive && isEmpty) {
            batch.draw(assetService.getActiveEmptyCellTexture(), getX(), getY());
        }
        super.draw(batch, parentAlpha);
    }

    public RoomBuildingModel getRoomModel() {
        return this.roomModel;
    }

    public void setRoomModel(RoomBuildingModel model) {
        if (model == null) {
            UiActorService uiActorService = UiActorService.getInstance();
            Office office = (Office) uiActorService.getActorById(OFFICE.name());
            getRoom().getRoomInfo().getStaff().forEach(office::removeActor);
            this.roomModel.getRoom().cancel(false);
            this.roomModel.setRoomInfo(null);
            this.roomModel.setRoom(null);
            this.roomModel = null;
            isEmpty = true;
            removeActor(this.buildStatus);
            this.objectCells.forEach(c -> c.forEach(this::removeActor));
            this.objectCells = new Array<>();
        } else {
            this.roomModel = model;
            if (!model.getRoom().isDone()) {
                this.buildStatus = new ProgressBar(0, model.getRoomInfo().getBuildTime().getDays(), 1f, false, this.skin);
                this.buildStatus.setWidth(getWidth() - DEFAULT_PAD / 2f);
                this.buildStatus.setPosition(this.buildStatus.getX() + DEFAULT_PAD / 4f,
                        this.buildStatus.getY() + getHeight() / 6f);
                this.buildTask = getBuildTask();
                addActor(this.buildStatus);
            }
            isEmpty = false;
        }
    }

    public boolean isBuilt() {
        return this.roomModel.getRoom().isDone();
    }

    public Room getRoom() {
        try {
            return this.roomModel.getRoom().get();
        } catch (InterruptedException | ExecutionException e) {
            Gdx.app.error("ERROR", "CANNOT OBTAIN INSTANCE OF ROOM. ERROR: " + e.getMessage());
        }
        return null;
    }

    public void setDetails(Room room) {
        this.objectCells = getObjectCells(this.roomModel.getRoomInfo().getType());
        this.objectCells.forEach(c -> c.forEach(this::addActor));
        UiActorService uiActorService = UiActorService.getInstance();
        Office office = (Office) uiActorService.getActorById(OFFICE.name());
        room.getRoomInfo().getStaff().forEach(office::addActor);
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
    private TimeLineTask<Room> getBuildTask() {
        for (TimeLineTask<Room> t : roomBuildService.getTasks()) {
            if (t.getId().equals(this.roomModel.getRoomInfo().getId())) {
                return t;
            }
        }
        return null;
    }

    private Array<Array<ObjectCell>> getObjectCells(Room.Type room) {
        Array<Array<ObjectCell>> cells = new Array<>();
        switch (room) {
            case HALL: {
                for (int i = 0; i <= OBJECT_CELL_SIZE + 1; i++) {
                    cells.insert(i, new Array<>());
                    for (int j = 0 ; j <= OBJECT_CELL_SIZE + 1; j++) {
                        cells.get(i).insert(j, new ObjectCell(OBJECT_CELL.name() + "#" + i + "#" + j + "_" + this.getName(),
                                i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, false));
                    }
                }
                break;
            }
            default: {
                for (int i = 0; i <= OBJECT_CELL_SIZE + 1; i++) {
                    cells.insert(i, new Array<>());
                    for (int j = 0 ; j <= OBJECT_CELL_SIZE + 1; j++) {
                        cells.get(i).insert(j,
                                i == 0 && j < OBJECT_CELL_SIZE / 2f && j > OBJECT_CELL_SIZE / 2f ?
                                new ObjectCell(OBJECT_CELL.name() + "#" + i + "#" + j + "_" + this.getName(),
                                i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, true)
                                : j == 0 && i < OBJECT_CELL_SIZE / 2f && i > OBJECT_CELL_SIZE / 2f ?
                                        new ObjectCell(OBJECT_CELL.name() + "#" + i + "#" + j + "_" + this.getName(),
                                                i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, true)
                                : i == OBJECT_CELL_SIZE - 1 && j < OBJECT_CELL_SIZE / 2f && j > OBJECT_CELL_SIZE / 2f ?
                                        new ObjectCell(OBJECT_CELL.name() + "#" + i + "#" + j + "_" + this.getName(),
                                                i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, true)
                                : j == OBJECT_CELL_SIZE - 1 && i < OBJECT_CELL_SIZE / 2f && i > OBJECT_CELL_SIZE / 2f ?
                                        new ObjectCell(OBJECT_CELL.name() + "#" + i + "#" + j + "_" + this.getName(),
                                                i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, true)
                                : new ObjectCell(OBJECT_CELL.name() + "#" + i + "#" + j + "_" + this.getName(),
                                        i * OBJECT_CELL_SIZE, j * OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, OBJECT_CELL_SIZE, false));
                    }
                }
                break;
            }
        }
        return cells;
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

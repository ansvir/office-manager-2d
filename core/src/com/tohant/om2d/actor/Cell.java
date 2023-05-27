package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.command.AbstractCommand;
import com.tohant.om2d.exception.GameException;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.model.task.TimeLineTask;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.AsyncRoomBuildService;
import com.tohant.om2d.stage.GameStage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.util.AssetsUtil.getDefaultSkin;

public class Cell extends Group {

    private final AbstractCommand command;

    private RoomBuildingModel roomModel;
    private boolean isEmpty;
    private boolean isActive;
    private final AssetService assetService;
    private ProgressBar buildStatus;
    private final Skin skin;
    private final AsyncRoomBuildService roomBuildService;
    private TimeLineTask<Room> buildTask;

//    public Cell(String id, float x, float y, float width, float height, Room room) {
//        super(id);
//        this.actor = new Group();
//        setPosition(x, y);
//        setSize(width, height);
//        this.roomModel = new RoomBuildingModel(CompletableFuture.supplyAsync(() -> room), room.getRoomInfo());
//        this.assetService = AssetService.getInstance();
//        this.skin = getDefaultSkin();
//        this.buildStatus = new ProgressBar(0, room.getRoomInfo().getBuildTime().getDays(), 1f, false, this.skin);
//        this.buildStatus.setWidth(getWidth() - DEFAULT_PAD / 2f);
//        this.buildStatus.setPosition(this.buildStatus.getX() + DEFAULT_PAD / 4f,
//                this.buildStatus.getY() + getHeight() / 6f);
//        actor.addActor(this.buildStatus);
//        this.roomBuildService = AsyncRoomBuildService.getInstance();
//        this.buildTask = getBuildTask();
//    }

    public Cell(String id, AbstractCommand command, float x, float y, float width, float height) {
        setName(id);
        this.command = command;
        setPosition(x, y);
        setSize(width, height);
        isEmpty = true;
        this.assetService = AssetService.getInstance();
        this.skin = getDefaultSkin();
        this.roomBuildService = AsyncRoomBuildService.getInstance();
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                try {
                    command.execute();
                } catch (GameException e) {
                    ((GameStage) getStage()).addException(e);
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
            this.roomModel.getRoom().cancel(false);
            this.roomModel.setRoomInfo(null);
            this.roomModel.setRoom(null);
            this.roomModel = null;
            isEmpty = true;
            removeActor(this.buildStatus);
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

}

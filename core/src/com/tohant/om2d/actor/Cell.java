package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncResult;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.model.task.RoomBuildingModel;
import com.tohant.om2d.service.AssetService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Cell extends Group {

    private RoomBuildingModel roomModel;
    private boolean isEmpty;
    private boolean isActive;
    private final AssetService assetService;

    public Cell(float x, float y, float width, float height, Room room) {
        setPosition(x, y);
        setSize(width, height);
        this.roomModel = new RoomBuildingModel(CompletableFuture.supplyAsync(() -> room), room.getRoomInfo());
        this.assetService = AssetService.getInstance();
    }

    public Cell(float x, float y, float width, float height) {
        setPosition(x, y);
        setSize(width, height);
        isEmpty = true;
        this.assetService = AssetService.getInstance();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isEmpty) {
            if (roomModel.getRoom().isDone()) {
                try {
                    roomModel.getRoom().get().draw(batch, parentAlpha);
                } catch (InterruptedException | ExecutionException ignored) {
                    if (roomModel.getRoom().isCancelled()) {
                        setRoomModel(null);
                    }
                }
            } else {
                batch.draw(assetService.getRoomConstructionTexture(), getX(), getY());
            }
        }
        if (isActive && isEmpty) {
            batch.draw(assetService.getActiveEmptyCellTexture(), getX(), getY());
        }
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
        } else {
            this.roomModel = model;
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

}

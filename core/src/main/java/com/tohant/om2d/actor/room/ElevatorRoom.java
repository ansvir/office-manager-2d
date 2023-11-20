package com.tohant.om2d.actor.room;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tohant.om2d.model.room.RoomInfo;
import com.tohant.om2d.service.AssetService;

public class ElevatorRoom extends Room {

    private final Type type = Type.ELEVATOR;

    public ElevatorRoom(String id, RoomInfo roomInfo, float width, float height) {
        super(id, roomInfo, width, height);
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!getRoomInfo().isUnderConstruction()) {
            batch.draw(AssetService.ELEVATOR_ROOM, getX(), getY());
        }
    }
}

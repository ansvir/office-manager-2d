package com.tohant.om2d.actor.room;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.model.room.RoomInfo;
import com.tohant.om2d.service.AssetService;

public abstract class Room extends Actor {

    private RoomInfo roomInfo;
    private final AssetService assetService;

    public Room(String id, RoomInfo roomInfo, float x, float y, float width, float height) {
        setName(id);
        this.roomInfo = roomInfo;
        setPosition(x, y);
        setSize(width, height);
        this.assetService = AssetService.getInstance();
    }

    public RoomInfo getRoomInfo() {
        return roomInfo;
    }

    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
    }

    public abstract Type getType();

    public enum Type {
        HALL(Color.GREEN, 100.0f, 20.0f),
        OFFICE(Color.RED, 550.0f, 50.0f),
        SECURITY(Color.BLUE, 910.0f, 100.0f),
        CLEANING(Color.YELLOW, 430.0f, 45.0f),
        CAFFE(Color.BLUE, 840.0f, 85.0f),
        ELEVATOR(Color.ORANGE, 1000.0f, 105.0f);

        private final Color color;
        private final float price;
        private final float cost;

        Type(Color color, float price, float cost) {
            this.color = new Color(color);
            this.color.a = 0.5f;
            this.price = price;
            this.cost = cost;
        }

        public Color getColor() {
            return color;
        }

        public float getPrice() {
            return price;
        }

        public float getCost() {
            return cost;
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Texture texture;
        if (getType() == Type.HALL) {
            texture = assetService.getHallRoomTexture();
        } else if (getType() == Type.SECURITY) {
            texture = assetService.getSecurityRoomTexture();
        } else if (getType() == Type.OFFICE) {
            texture = assetService.getOfficeRoomTexture();
        } else if (getType() == Type.CLEANING) {
            texture = assetService.getCleaningRoomTexture();
        } else if (getType() == Type.CAFFE) {
            texture = assetService.getCaffeRoomTexture();
        } else {
            texture = assetService.getElevatorRoomTexture();
        }
        batch.draw(texture, getX(), getY());
    }

}

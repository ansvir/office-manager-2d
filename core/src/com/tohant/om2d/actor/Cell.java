package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tohant.om2d.actor.room.Room;
import com.tohant.om2d.service.AssetService;

public class Cell extends Group {

//    private final static int BORDER_BOLDNESS = 2;

    private Room room;
    private boolean isEmpty;
    private boolean isActive;
    private final AssetService assetService;

    public Cell(float x, float y, float width, float height, Room room) {
        setPosition(x, y);
        setSize(width, height);
        this.room = room;
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
            room.draw(batch, parentAlpha);
        }
        if (isActive && isEmpty) {
            batch.draw(assetService.getActiveEmptyCell(), getX(), getY());
        }
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        if (room == null) {
            this.room = null;
            isEmpty = true;
        } else {
            this.room = room;
            isEmpty = false;
        }
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


//    private void setTexture() {
//        Pixmap cellPixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
//        Color bordersColor = Color.BLACK;
//        bordersColor.a = 0.2f;
//        cellPixmap.setColor(bordersColor);
//        cellPixmap.fill();
//        texture = new Texture(cellPixmap);
//        cellPixmap.dispose();
//    }
//
//    private void setTextureForNonEmpty() {
//        Pixmap borders = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
//        Color bordersColor = Color.BLACK;
//        bordersColor.a = 0.5f;
//        borders.setColor(bordersColor);
//        for (int i = 0; i < BORDER_BOLDNESS - 1; i++) {
//            borders.drawRectangle(i + 1, i + 1, (int) getWidth() - i - 1, (int) getHeight() - i - 1);
//        }
//        texture = new Texture(borders);
//        borders.dispose();
//    }

//    @Override
//    public void dispose() {
//        this.texture.dispose();
//    }

}

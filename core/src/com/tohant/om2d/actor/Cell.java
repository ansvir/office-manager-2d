package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Cell extends Group {

    private final static int BORDER_BOLDNESS = 2;

    private Room room;
    private boolean isEmpty;
    private Texture texture;
    private boolean isActive;

    public Cell(float x, float y, float width, float height, Room room) {
        setPosition(x, y);
        setSize(width, height);
        this.room = room;
        setTextureForNonEmpty();
    }

    public Cell(float x, float y, float width, float height) {
        setPosition(x, y);
        setSize(width, height);
        isEmpty = true;
        setTexture();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isEmpty) {
            room.draw(batch, parentAlpha);
        }
        if (isActive) {
            batch.draw(texture, getX(), getY());
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

    private void setTexture() {
        Pixmap borders = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        Color bordersColor = Color.BLACK;
        bordersColor.a = 0.2f;
        borders.setColor(bordersColor);
        borders.fill();
        texture = new Texture(borders);
        borders.dispose();
    }

    private void setTextureForNonEmpty() {
        Pixmap borders = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        Color bordersColor = Color.BLACK;
        bordersColor.a = 0.5f;
        borders.setColor(bordersColor);
        for (int i = 0; i < BORDER_BOLDNESS - 1; i++) {
            borders.drawRectangle(i + 1, i + 1, (int) getWidth() - i - 1, (int) getHeight() - i - 1);
        }
        texture = new Texture(borders);
        borders.dispose();
    }

}

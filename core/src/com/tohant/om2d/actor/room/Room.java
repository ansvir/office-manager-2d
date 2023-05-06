package com.tohant.om2d.actor.room;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.tohant.om2d.actor.man.Staff;
import com.tohant.om2d.util.AssetsUtil;

import java.util.UUID;

public abstract class Room extends Actor implements Disposable {

    private String id;
    private String number;
    private Texture texture;
    private float price;
    private float cost;
    private final Array<Staff> staff;

    public Room(String id, Array<Staff> staff, float price, float cost, float x, float y, float width, float height) {
        this.id = id;
        this.price = price;
        this.cost = cost;
        this.number = this.id.substring(0, 4);
        this.staff = staff;
        setPosition(x, y);
        setSize(width, height);
    }

    public Room(Array<Staff> staff, float price, float cost, float x, float y, float width, float height) {
        this.id = UUID.randomUUID().toString();
        this.price = price;
        this.cost = cost;
        this.number = this.id.substring(0, 4);
        this.staff = staff;
        setPosition(x, y);
        setSize(width, height);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public abstract Type getType();

    public enum Type {
        HALL(Color.GREEN, 100.0f, 20.0f),
        OFFICE(Color.RED, 550.0f, 50.0f),
        SECURITY(Color.BLUE, 910.0f, 100.0f),
        CLEANING(Color.YELLOW, 430.0f, 45.0f);

        private final Color color;
        private final float price;
        private final float cost;

        Type(Color color, float price, float cost) {
            this.color = color;
            color.a = 0.5f;
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
        if (getTexturePath() != null) {
            Texture newTexture = new Texture(getTexturePath());
            texture = AssetsUtil.resizeTexture(newTexture, getWidth(), getHeight());
            newTexture.dispose();
        } else {
            Pixmap pixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
            pixmap.setColor(getType().getColor());
            pixmap.fillRectangle(1, 1, pixmap.getWidth() - 1, pixmap.getHeight() - 1);
            texture = new Texture(pixmap);
            pixmap.dispose();
        }
        batch.draw(texture, getX(), getY());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    public abstract String getTexturePath();

    public Array<Staff> getStaff() {
        return staff;
    }

}

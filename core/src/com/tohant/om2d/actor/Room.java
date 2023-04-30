package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

import java.util.UUID;

public abstract class Room extends Actor implements Disposable {

    private String id;
    private Texture texture;
    private float price;

    public Room(String id, float price) {
        this.id = id;
        this.price = price;
    }

    public Room(float price) {
        this.id = UUID.randomUUID().toString();
        this.price = price;
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

    public abstract Type getType();

    public enum Type {
        HALL(Color.GREEN), OFFICE(Color.RED);

        private final Color color;
        Type(Color color) {
            this.color = color;
            color.a = 0.5f;
        }

        public Color getColor() {
            return color;
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Pixmap pixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(getType().getColor());
        pixmap.fillRectangle(1, 1, pixmap.getWidth() - 1, pixmap.getHeight() - 1);
        texture = new Texture(pixmap);
        batch.draw(texture, getX(), getY());
        pixmap.dispose();
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

}

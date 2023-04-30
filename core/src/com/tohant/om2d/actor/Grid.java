package com.tohant.om2d.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;

public class Grid extends Group implements Disposable {

    private float cellSize;
    private Texture texture;

    public Grid(int x, int y, float width, float height, int cellSize) {
        setPosition(x, y);
        setSize(width + 1, height + 1);
        this.cellSize = cellSize;
        Pixmap pixmap = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        Color borderColor = Color.GRAY;
        borderColor.a = 0.5f;
        pixmap.setColor(borderColor);
        for (int i = 0; i < getWidth(); i += cellSize) {
            for (int j = 0; j < getHeight(); j += cellSize) {
                pixmap.drawLine(i, j, i, j + cellSize);
                pixmap.drawLine(i, j, i + cellSize, j);
                addActor(new Cell(i, getHeight() - j, cellSize, cellSize));
            }
        }
        texture = new Texture(pixmap);
        pixmap.dispose();
    }

    public float getCellSize() {
        return cellSize;
    }

    public void setCellSize(float cellSize) {
        this.cellSize = cellSize;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

}
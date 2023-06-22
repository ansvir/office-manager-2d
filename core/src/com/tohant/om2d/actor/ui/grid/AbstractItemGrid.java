package com.tohant.om2d.actor.ui.grid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.tohant.om2d.actor.Item;

import static com.tohant.om2d.actor.constant.Constant.DEFAULT_PAD;
import static com.tohant.om2d.actor.constant.Constant.TEXTURE_SIZE;

public abstract class AbstractItemGrid extends Table {

    public AbstractItemGrid(String id, Array<Item> items) {
        setName(id);
        Vector2 gridSize = getGridSize(items.size);
        setSize(gridSize.x * TEXTURE_SIZE + DEFAULT_PAD * gridSize.x + DEFAULT_PAD * 2,
                gridSize.y * TEXTURE_SIZE + DEFAULT_PAD * gridSize.y + DEFAULT_PAD * 2);
        populateGrid(items);
    }

    protected abstract void populateGrid(Array<Item> items);

    protected Vector2 getGridSize(int size) {
        int columns = Math.round(Gdx.graphics.getWidth() / TEXTURE_SIZE / 7);
        int rows = columns / size;
        return new Vector2(columns, Math.round(rows - 1));
    }

}

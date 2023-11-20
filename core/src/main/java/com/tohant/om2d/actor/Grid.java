package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;
import com.tohant.om2d.service.AsyncRoomBuildService;

import static com.tohant.om2d.actor.constant.Constant.*;

public class Grid extends Group implements Disposable, ToggleActor {

    private Texture texture;
    private AsyncRoomBuildService roomBuildService;
    private boolean isGridVisible;

    public Grid(String id) {
        setName(id);
        isGridVisible = true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (isGridVisible) {
            batch.draw(texture, getX(), getY(), GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE);
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    public void setIsGridVisible(boolean isVisible) {
        this.isGridVisible = isVisible;
    }

    @Override
    public void toggle() {
        setIsGridVisible(!isGridVisible);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

}
package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cell extends Group implements ToggleActor {

    private final Texture activeEmptyCellTexture;
    private boolean isEmpty;
    private boolean isActive;
    private boolean isGridVisible;

    public Cell(String id, Texture activeEmptyCellTexture, float x, float y, float width, float height) {
        setName(id);
        setPosition(x, y);
        setSize(width, height);
        this.activeEmptyCellTexture = activeEmptyCellTexture;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isActive && isEmpty) {
            batch.draw(activeEmptyCellTexture, getX(), getY());
        }
        super.draw(batch, parentAlpha);
    }

    @Override
    public void toggle() {
        isGridVisible = !isGridVisible;
    }

    @Override
    public void forceToggle(boolean value) {
        isGridVisible = value;
    }

}

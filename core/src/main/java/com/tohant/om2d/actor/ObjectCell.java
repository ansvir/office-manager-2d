package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.tohant.om2d.service.AssetService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectCell extends Group {

    private boolean isObstacle;
    private boolean isActive;
    private boolean isGridVisible;

    public ObjectCell(String id, float x, float y, float width, float height, boolean isObstacle) {
        setName(id);
        setPosition(x, y);
        setSize(width, height);
        this.isObstacle = isObstacle;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (isGridVisible) {
            if (isActive) {
                batch.draw(AssetService.ACTIVE_OBJECT_CELL, getX(), getY());
            } else {
                batch.draw(AssetService.OBJECT_CELL_BORDERS, getX(), getY());
            }
        }
    }

}

package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.GameActorFactory;
import lombok.Getter;

@Getter
public class ObjectCellItem extends Actor {

    private final GameActorFactory.UiComponentConstant.Items type;

    public ObjectCellItem(String id, GameActorFactory.UiComponentConstant.Items type) {
        setName(id);
        this.type = type;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        switch (this.type) {
            case PLANT: batch.draw(AssetService.Items.PLANT_CELL, getX(), getY()); break;
            default: batch.draw(AssetService.Items.COOLER_CELL, getX(), getY()); break;
        }
    }

}

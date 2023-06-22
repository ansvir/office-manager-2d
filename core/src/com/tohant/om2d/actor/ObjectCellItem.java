package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.UiActorService;

import static com.tohant.om2d.actor.constant.Constant.OBJECT_CELL_SIZE;

public class ObjectCellItem extends Actor {

    private final UiActorService.UiComponentConstant.Items type;

    public ObjectCellItem(UiActorService.UiComponentConstant.Items type) {
        setSize(OBJECT_CELL_SIZE, OBJECT_CELL_SIZE);
        this.type = type;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        switch (this.type) {
            case PLANT: batch.draw(AssetService.getInstance().getItems().getPlantCellTexture(), getX(), getY()); break;
            default: batch.draw(AssetService.getInstance().getItems().getCoolerCellTexture(), getX(), getY()); break;
        }
    }

    public UiActorService.UiComponentConstant.Items getType() {
        return type;
    }

}

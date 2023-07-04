package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.UiActorService;

public class Item extends Image {

    private final Texture texture;
    private final UiActorService.UiComponentConstant.Items type;

    public Item(UiActorService.UiComponentConstant.Items type) {
        setName(type.name() + "_ITEM");
        this.type = type;
        switch (this.type) {
            case PLANT: this.texture = AssetService.getInstance().getItems().getPlantTexture(); break;
            default: this.texture = AssetService.getInstance().getItems().getCoolerTexture(); break;
        }
        setDrawable((new TextureRegionDrawable(new TextureRegion(this.texture))));
    }

    public UiActorService.UiComponentConstant.Items getType() {
        return type;
    }

}

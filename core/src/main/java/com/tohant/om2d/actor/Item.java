package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tohant.om2d.service.AssetService;
import com.tohant.om2d.service.GameActorFactory;
import lombok.Getter;

public class Item extends Image {

    private final Texture texture;
    @Getter
    private final GameActorFactory.UiComponentConstant.Items type;

    public Item(GameActorFactory.UiComponentConstant.Items type) {
        setName(type.name());
        this.type = type;
        switch (this.type) {
            case PLANT: this.texture = AssetService.Items.PLANT; break;
            default: this.texture = AssetService.Items.COOLER; break;
        }
        setDrawable(new TextureRegionDrawable(new TextureRegion(this.texture)));
    }

}

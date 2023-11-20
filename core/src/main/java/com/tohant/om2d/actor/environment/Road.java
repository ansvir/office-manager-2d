package com.tohant.om2d.actor.environment;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.model.RoadType;
import com.tohant.om2d.service.AssetService;
import lombok.Getter;
import lombok.Setter;

import static com.tohant.om2d.actor.constant.Constant.TEXTURE_SIZE;

@Getter
@Setter
public class Road extends Actor {

    private RoadType type;

    public Road(String id, float x, float y, RoadType type) {
        setName(id);
        this.type = type;
        setPosition(x, y);
        setSize(TEXTURE_SIZE, TEXTURE_SIZE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(getRoadTypeTexture(this.type), getX(), getY());
    }

    private TextureRegion getRoadTypeTexture(RoadType type) {
        TextureRegion texture = null;
        switch (type) {
            case RIGHT: texture = AssetService.VR_ROAD; break;
            case LEFT: texture = AssetService.VL_ROAD; break;
            case TOP: texture = AssetService.HU_ROAD; break;
            case BOTTOM: texture = AssetService.HD_ROAD; break;
            case EMPTY: texture = AssetService.EMPTY_ROAD; break;
        }
        return texture;
    }

}

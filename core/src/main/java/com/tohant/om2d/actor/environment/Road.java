package com.tohant.om2d.actor.environment;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tohant.om2d.model.RoadType;
import com.tohant.om2d.service.AssetService;

import static com.tohant.om2d.actor.constant.Constant.TEXTURE_SIZE;

/**
 * Road actor. Serves as background texture for cars or any other transport.
 */
public class Road extends Actor {

    private RoadType type;

    public Road(String id, float x, float y, RoadType type) {
        setName(id);
        this.type = type;
        setPosition(x, y);
        setSize(TEXTURE_SIZE, TEXTURE_SIZE);
    }

    public RoadType getType() {
        return type;
    }

    public void setType(RoadType type) {
        this.type = type;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(getRoadTypeTexture(this.type), getX(), getY());
    }

    private static TextureRegion getRoadTypeTexture(RoadType type) {
        AssetService assetService = AssetService.getInstance();
        TextureRegion texture = null;
        switch (type) {
            case RIGHT: texture = assetService.getVRRoadTexture(); break;
            case LEFT: texture = assetService.getVLRoadTexture(); break;
            case TOP: texture = assetService.getHURoadTexture(); break;
            case BOTTOM: texture = assetService.getHDRoadTexture(); break;
            case EMPTY: texture = assetService.getEmptyRoadTexture(); break;
        }
        return texture;
    }
}

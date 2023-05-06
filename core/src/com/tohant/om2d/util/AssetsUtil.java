package com.tohant.om2d.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetsUtil {

    public static Skin getDefaultSkin() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skins/plain-j/plain-james.atlas"));
        return new Skin(Gdx.files.internal("skins/plain-j/plain-james.json"), atlas);
    }

    public static Pixmap resizePixmap(Pixmap pixmap, float width, float height) {
        Pixmap newPixmap = new Pixmap((int) width, (int) height, pixmap.getFormat());
        pixmap.setFilter(Pixmap.Filter.NearestNeighbour);
        newPixmap.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 0, 0, (int) width, (int) height);
        pixmap.dispose();
        return newPixmap;
    }

    public static Texture resizeTexture(Texture texture, float width, float height) {
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        Pixmap pixmap = texture.getTextureData().consumePixmap();
        Pixmap newPixmap = new Pixmap((int) width, (int) height, pixmap.getFormat());
        pixmap.setFilter(Pixmap.Filter.NearestNeighbour);
        newPixmap.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 0, 0, (int) width, (int) height);
        pixmap.dispose();
        return new Texture(newPixmap);
    }

    public static Texture resizeTexture(String internalPath, float width, float height) {
        Pixmap pixmap = new Pixmap(Gdx.files.internal(internalPath));
        Pixmap newPixmap = new Pixmap((int) width, (int) height, pixmap.getFormat());
        pixmap.setFilter(Pixmap.Filter.NearestNeighbour);
        newPixmap.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 0, 0, (int) width, (int) height);
        pixmap.dispose();
        return new Texture(newPixmap);
    }

}

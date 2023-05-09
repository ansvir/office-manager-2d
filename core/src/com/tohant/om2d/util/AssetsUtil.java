package com.tohant.om2d.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetsUtil {

    private static final String PLAIN_J_ATLAS = "skins/plain-j/plain-james.atlas";
    private static final String PLAIN_J_SKIN = "skins/plain-j/plain-james.json";
    private static final String NEON_UI_ATLAS = "skins/neon-ui/neon-ui.atlas";
    private static final String NEON_UI_JSON = "skins/neon-ui/neon-ui.json";
    private static final String ORANGE_ATLAS = "skins/orange/uiskin.atlas";
    private static final String ORANGE_JSON = "skins/orange/uiskin.json";
    private static final TextureAtlas ATLAS = new TextureAtlas(Gdx.files.internal(ORANGE_ATLAS));
    private static final Skin SKIN = new Skin(Gdx.files.internal(ORANGE_JSON), ATLAS);

    public static Skin getDefaultSkin() {
        return SKIN;
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

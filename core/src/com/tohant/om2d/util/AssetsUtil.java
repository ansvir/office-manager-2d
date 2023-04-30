package com.tohant.om2d.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetsUtil {

    public static Skin getDefaultSkin() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("skins/plain-j/plain-james.atlas"));
        return new Skin(Gdx.files.internal("skins/plain-j/plain-james.json"), atlas);
    }

}

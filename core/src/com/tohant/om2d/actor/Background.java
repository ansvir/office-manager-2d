package com.tohant.om2d.actor;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class Background extends Actor implements Disposable {

    private Texture background;

    public Background() {
        createGrass();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(background, getX(), getY());
    }

    private void createGrass() {
        int width = Math.round(getWidth() / 64f);
        int height = Math.round(getHeight() / 64f);
        Texture grass = new Texture("grass.png");
        Pixmap grassBackground = new Pixmap((int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888);
        if (!grass.getTextureData().isPrepared()) {
            grass.getTextureData().prepare();
        }
        Pixmap grassPixmap = grass.getTextureData().consumePixmap();
        for (int i = 0 ; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grassBackground.drawPixmap(grassPixmap, i * 64, j * 64);
            }
        }
        background = new Texture(grassBackground);
        grassPixmap.dispose();
        grassBackground.dispose();
    }

    @Override
    public void dispose() {
        this.background.dispose();
    }
}
